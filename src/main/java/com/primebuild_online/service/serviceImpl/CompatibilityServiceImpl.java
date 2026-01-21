package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.DTO.BuildReqDTO;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemFeature;
import com.primebuild_online.service.CompatibilityService;
import com.primebuild_online.service.ComponentService;
import com.primebuild_online.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompatibilityServiceImpl implements CompatibilityService {

    private final ItemService itemService;
    private final ComponentService componentService;

    public CompatibilityServiceImpl(ItemService itemService, ComponentService componentService) {
        this.itemService = itemService;
        this.componentService = componentService;
    }

    public List<Item> getCompatibleItemsByComponent(BuildReqDTO buildReqDTO, Long componentId) {

        System.out.println("\n================ START COMPATIBILITY CHECK ================\n");

//        extract build item IDs
        List<Long> buildItemIds = buildReqDTO.getItemList()
                .stream()
                .map(Item::getId)
                .toList();

        System.out.println("Build Item IDs: " + buildItemIds);

//        load build items
        List<Item> buildItems = itemService.getItemsByIds(buildItemIds);

//        prepare feature maps
        Map<Long, Set<Long>> requiredByType = new HashMap<>();
        Map<Long, Long> lockedByType = new HashMap<>();

//        analyze build items
        for (Item item : buildItems) {

            System.out.println("\n--- Build Item ---");
            System.out.println(item.getId() + " | " +
                    item.getComponent().getComponentName() + " | " +
                    item.getItemName());

            Map<Long, Set<Long>> itemFeaturesByType = new HashMap<>();

            for (ItemFeature f : item.getItemFeatureList()) {

                Long typeId = f.getFeature().getFeatureType().getId();
                Long featureId = f.getFeature().getId();

                itemFeaturesByType
                        .computeIfAbsent(typeId, k -> new HashSet<>())
                        .add(featureId);

                requiredByType
                        .computeIfAbsent(typeId, k -> new HashSet<>())
                        .add(featureId);

                System.out.println("FeatureType " + typeId +
                        " Feature " + featureId +
                        " (" + f.getFeature().getFeatureName() + ")");
            }

//            lock single-feature components
            for (Map.Entry<Long, Set<Long>> entry : itemFeaturesByType.entrySet()) {
                if (entry.getValue().size() == 1) {
                    Long typeId = entry.getKey();
                    Long lockedFeature = entry.getValue().iterator().next();
                    lockedByType.put(typeId, lockedFeature);

                    System.out.println("LOCKED FeatureType " + typeId +
                            " Feature " + lockedFeature);
                }
            }
        }

        System.out.println("\nRequired Features By Type:");
        requiredByType.forEach((k, v) ->
                System.out.println("FeatureType " + k + v)
        );

        System.out.println("\nLocked Features:");
        lockedByType.forEach((k, v) ->
                System.out.println("FeatureType " + k + v)
        );

//        load candidate items
        List<Item> candidates =
                itemService.getInStockItemListByComponent(componentId);

        List<Item> compatibleItemList = new ArrayList<>();

//        compatibility check
        for (Item candidate : candidates) {

            System.out.println("\n====================================");
            System.out.println("Checking Candidate:");
            System.out.println(candidate.getId() + " | " +
                    candidate.getComponent().getComponentName() + " | " +
                    candidate.getItemName());

            boolean compatible = true;

//             group candidate features by FeatureType
            Map<Long, Set<Long>> candidateByType = new HashMap<>();

            for (ItemFeature cf : candidate.getItemFeatureList()) {
                candidateByType
                        .computeIfAbsent(
                                cf.getFeature().getFeatureType().getId(),
                                k -> new HashSet<>()
                        )
                        .add(cf.getFeature().getId());
            }

            if (candidateByType.isEmpty()) {
                compatible = false;
            }

//            only check feature types the candidate actually has
            for (Map.Entry<Long, Set<Long>> entry : candidateByType.entrySet()) {

                Long typeId = entry.getKey();
                Set<Long> candidateFeatures = entry.getValue();

                System.out.println("FeatureType " + typeId +
                        " Candidate Features: " + candidateFeatures);

//                locked rule
                if (lockedByType.containsKey(typeId)) {

                    Long lockedFeature = lockedByType.get(typeId);
                    System.out.println("Locked Required Feature: " + lockedFeature);

                    if (!candidateFeatures.contains(lockedFeature)) {
                        System.out.println("No matching feature → NOT COMPATIBLE");
                        compatible = false;
                        break;
                    } else {
                        System.out.println("Matches LOCKED feature");
                    }
                }

//                shared rule
                else if (requiredByType.containsKey(typeId)) {

                    Set<Long> allowed = requiredByType.get(typeId);
                    System.out.println("Allowed Features: " + allowed);

                    boolean anyMatch = candidateFeatures.stream()
                            .anyMatch(allowed::contains);

                    if (!anyMatch) {
                        System.out.println("No shared feature → NOT COMPATIBLE");
                        compatible = false;
                        break;
                    } else {
                        System.out.println("At least one feature matches");
                    }
                }
//                if build does not care about this FeatureType then SKIP
            }

            if (compatible) {
                System.out.println("FINAL RESULT: COMPATIBLE");
                compatibleItemList.add(candidate);
            } else {
                System.out.println("FINAL RESULT: NOT COMPATIBLE");
            }
        }

        System.out.println("\n================ END COMPATIBILITY CHECK ================");
        System.out.println("Total Compatible Items: " + compatibleItemList.size());

        return compatibleItemList;
    }

    @Override
    public List<Item> getCompatiblePowerSources(BuildReqDTO buildReqDTO, Long componentId, Boolean powerSource) {
        System.out.println("\n================ START POWER SOURCE COMPATIBILITY CHECK ================\n");

        List<Long> buildItemIds = buildReqDTO.getItemList()
                .stream()
                .map(Item::getId)
                .toList();

        List<Item> buildItems = itemService.getItemsByIds(buildItemIds);

        float totalPower = 0;

        for (Item item : buildItems) {
            totalPower += item.getPowerConsumption();
            System.out.println("\n--- Build Item ---");
            System.out.println(item.getId() + " | " +
                    item.getComponent().getComponentName() + " | " +
                    item.getItemName());

            System.out.println("Power Consumption " + item.getPowerConsumption() + "W");
        }

        System.out.println("\n====================================");

        System.out.println("Total Power Consumption: " + totalPower + "W");

        Component componentInDb = componentService.getComponentById(componentId);

        List<Item> compatiblePowerSources = new ArrayList<>();

        float powerSourceOutput;

        if (componentInDb.isPowerSource()) {

            for (Item cadidate : componentInDb.getItemList()) {

                powerSourceOutput = cadidate.getPowerConsumption();

                System.out.println("\n====================================");

                System.out.println("Checking Candidate:");

                System.out.println(cadidate.getId() + " | " +
                        cadidate.getComponent().getComponentName() + " | " +
                        cadidate.getItemName());

                System.out.println("Power Consumption: " + cadidate.getPowerConsumption() + "W");

                if (powerSourceOutput >= totalPower) {

                    System.out.println("FINAL RESULT: COMPATIBLE");

                    compatiblePowerSources.add(cadidate);

                } else {

                    System.out.println("FINAL RESULT: NOT COMPATIBLE");
                }
            }
        }

        System.out.println("\n================ END POWER SOURCE COMPATIBILITY CHECK ================");
        System.out.println("Total Compatible Power Sources: " + compatiblePowerSources.size());

        return compatiblePowerSources;
    }


}
