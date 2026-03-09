package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.DTO.BuildReqDTO;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemFeature;
import com.primebuild_online.service.CompatibilityService;
import com.primebuild_online.service.ComponentService;
import com.primebuild_online.service.ItemService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompatibilityServiceImpl implements CompatibilityService {

    private final ItemService itemService;
    private final ComponentService componentService;

    public CompatibilityServiceImpl(
            @Lazy ItemService itemService,
            ComponentService componentService) {
        this.itemService = itemService;
        this.componentService = componentService;
    }

    @Override
    public Page<Item> getCompatibleItemsByComponent(BuildReqDTO buildReqDTO, Long componentId, Pageable pageable) {

        System.out.println("\n================ START COMPATIBILITY CHECK ================\n");

        // extract build item IDs
        List<Long> buildItemIds = buildReqDTO.getItemList()
                .stream()
                .map(Item::getId)
                .toList();

        System.out.println("Build Item IDs: " + buildItemIds);

        // load build items
        List<Item> buildItems = itemService.getItemsByIds(buildItemIds);

        // map build item quantity by ID
        Map<Long, Integer> buildItemQuantities = buildReqDTO.getItemList()
                .stream()
                .collect(Collectors.toMap(Item::getId, Item::getQuantity));

        // required features and locked features maps
        Map<Long, Set<Long>> requiredByType = new HashMap<>();
        Map<Long, Long> lockedByType = new HashMap<>();

        // slotCounts per FeatureType+Feature
        Map<Long, List<Integer>> slotCountsByTypeAndFeature = new HashMap<>();

        // analyze build items
        for (Item item : buildItems) {
            int itemQuantity = buildItemQuantities.getOrDefault(item.getId(), 1);

            System.out.println("\n--- Build Item ---");
            System.out.println(item.getId() + " | " +
                    item.getComponent().getComponentName() + " | " +
                    item.getItemName() + " | Quantity: " + itemQuantity);

            Map<Long, Set<Long>> itemFeaturesByType = new HashMap<>();

            for (ItemFeature f : item.getItemFeatureList()) {
                Long typeId = f.getFeature().getFeatureType().getId();
                Long featureId = f.getFeature().getId();

                itemFeaturesByType.computeIfAbsent(typeId, k -> new HashSet<>()).add(featureId);

                // store slotCount for later min calculation
                Long key = typeId * 1000000 + featureId;
                slotCountsByTypeAndFeature.computeIfAbsent(key, k -> new ArrayList<>())
                        .add(f.getSlotCount() * itemQuantity); // multiply by item quantity

                System.out.println("FeatureType " + typeId +
                        " Feature " + featureId +
                        " (" + f.getFeature().getFeatureName() + "), SlotCount=" + f.getSlotCount() +
                        ", Total Required Slots=" + (f.getSlotCount() * itemQuantity));
            }

            // intersection logic for required features
            for (Map.Entry<Long, Set<Long>> entry : itemFeaturesByType.entrySet()) {
                Long typeId = entry.getKey();
                Set<Long> features = entry.getValue();
                requiredByType.compute(typeId, (k, v) -> {
                    if (v == null) return new HashSet<>(features);
                    v.retainAll(features);
                    return v;
                });
            }

            // lock single-feature components
            for (Map.Entry<Long, Set<Long>> entry : itemFeaturesByType.entrySet()) {
                if (entry.getValue().size() == 1) {
                    Long typeId = entry.getKey();
                    Long lockedFeature = entry.getValue().iterator().next();
                    lockedByType.put(typeId, lockedFeature);
                    System.out.println("LOCKED FeatureType " + typeId + " Feature " + lockedFeature);
                }
            }
        }

        System.out.println("\nRequired Features By Type:");
        requiredByType.forEach((k, v) -> System.out.println("FeatureType " + k + " -> " + v));

        System.out.println("\nLocked Features:");
        lockedByType.forEach((k, v) -> System.out.println("FeatureType " + k + " -> " + v));

        // load candidate items
        List<Item> candidates = itemService.getInStockItemListByComponentForCompatibility(componentId);
        List<Item> compatibleItemList = new ArrayList<>();

        // compatibility check
        for (Item candidate : candidates) {

            System.out.println("\n====================================");
            System.out.println("Checking Candidate:");
            System.out.println(candidate.getId() + " | " +
                    candidate.getComponent().getComponentName() + " | " +
                    candidate.getItemName());

            boolean compatible = true;

            // group candidate features by FeatureType
            Map<Long, Set<Long>> candidateByType = new HashMap<>();
            Map<Long, Map<Long, Integer>> candidateSlotMap = new HashMap<>(); // typeId -> featureId -> slotCount
            for (ItemFeature cf : candidate.getItemFeatureList()) {
                Long typeId = cf.getFeature().getFeatureType().getId();
                Long featureId = cf.getFeature().getId();

                candidateByType.computeIfAbsent(typeId, k -> new HashSet<>()).add(featureId);
                candidateSlotMap.computeIfAbsent(typeId, k -> new HashMap<>()).put(featureId, cf.getSlotCount());
            }

            // check each candidate feature
            for (Map.Entry<Long, Set<Long>> entry : candidateByType.entrySet()) {
                Long typeId = entry.getKey();
                Set<Long> candidateFeatures = entry.getValue();

                Long matchedFeatureId = null;

                // locked rule
                if (lockedByType.containsKey(typeId)) {
                    Long lockedFeature = lockedByType.get(typeId);
                    if (!candidateFeatures.contains(lockedFeature)) {
                        compatible = false;
                        System.out.println("Locked feature not present → NOT COMPATIBLE");
                        break;
                    } else matchedFeatureId = lockedFeature;
                }
                // shared rule
                else if (requiredByType.containsKey(typeId)) {
                    Optional<Long> match = candidateFeatures.stream()
                            .filter(requiredByType.get(typeId)::contains)
                            .findFirst();
                    if (match.isEmpty()) {
                        compatible = false;
                        System.out.println("No shared feature → NOT COMPATIBLE");
                        break;
                    } else matchedFeatureId = match.get();
                }

                // check slotCount against build required slots (min across build items)
                if (matchedFeatureId != null) {
                    Long key = typeId * 1000000 + matchedFeatureId;
                    List<Integer> requiredSlotsList = slotCountsByTypeAndFeature.get(key);

                    if (requiredSlotsList != null && !requiredSlotsList.isEmpty()) {
                        int minRequiredSlots = requiredSlotsList.stream()
                                .min(Integer::compareTo)
                                .orElse(1);

                        int candidateSlot = candidateSlotMap.get(typeId).get(matchedFeatureId);

                        System.out.println("FeatureType " + typeId +
                                ", Feature " + matchedFeatureId +
                                ", Candidate SlotCount=" + candidateSlot +
                                ", Min Required Slots from Build=" + minRequiredSlots);

                        if (candidateSlot < minRequiredSlots) {
                            compatible = false;
                            System.out.println("Candidate slotCount < min required slots → NOT COMPATIBLE");
                            break;
                        }
                    }
                }
            }

            if (compatible) {
                compatibleItemList.add(candidate);
                System.out.println("FINAL RESULT: COMPATIBLE");
            } else {
                System.out.println("FINAL RESULT: NOT COMPATIBLE");
            }
        }

        System.out.println("\n================ END COMPATIBILITY CHECK ================");
        System.out.println("Total Compatible Items: " + compatibleItemList.size());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), compatibleItemList.size());

        List<Item> subList;
        if (start <= end) {
            subList = compatibleItemList.subList(start, end);
        } else {
            subList = Collections.emptyList();
        }

        return new PageImpl<>(subList, pageable, compatibleItemList.size());
    }


    @Override
    public List<Item> getCompatiblePowerSources(BuildReqDTO buildReqDTO, Long componentId, Boolean powerSource) {
//        System.out.println("\n================ START POWER SOURCE COMPATIBILITY CHECK ================\n");

        List<Long> buildItemIds = buildReqDTO.getItemList()
                .stream()
                .map(Item::getId)
                .toList();

        List<Item> buildItems = itemService.getItemsByIds(buildItemIds);

        float totalPower = 0;

        for (Item item : buildItems) {
            totalPower += item.getPowerConsumption();
//            System.out.println("\n--- Build Item ---");
//            System.out.println(item.getId() + " | " +
//                    item.getComponent().getComponentName() + " | " +
//                    item.getItemName());
//
//            System.out.println("Power Consumption " + item.getPowerConsumption() + "W");
        }

//        System.out.println("\n====================================");

//        System.out.println("Total Power Consumption: " + totalPower + "W");

        Component componentInDb = componentService.getComponentById(componentId);

        List<Item> compatiblePowerSources = new ArrayList<>();

        float powerSourceOutput;

        if (componentInDb.isPowerSource()) {

            for (Item cadidate : componentInDb.getItemList()) {

                powerSourceOutput = cadidate.getPowerConsumption();

//                System.out.println("\n====================================");

//                System.out.println("Checking Candidate:");

                System.out.println(cadidate.getId() + " | " +
                        cadidate.getComponent().getComponentName() + " | " +
                        cadidate.getItemName());

//                System.out.println("Power Consumption: " + cadidate.getPowerConsumption() + "W");

                if (powerSourceOutput >= totalPower) {

//                    System.out.println("FINAL RESULT: COMPATIBLE");
//                    cadidate.setQuantity(1);
//                    compatiblePowerSources.add(cadidate);

//                } else {
//
//                    System.out.println("FINAL RESULT: NOT COMPATIBLE");
                }
            }
        }

//        System.out.println("\n================ END POWER SOURCE COMPATIBILITY CHECK ================");
//        System.out.println("Total Compatible Power Sources: " + compatiblePowerSources.size());

        return compatiblePowerSources;
    }


}
