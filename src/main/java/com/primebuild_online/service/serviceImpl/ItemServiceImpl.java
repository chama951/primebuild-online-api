package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.*;
import com.primebuild_online.model.DTO.ItemReqDTO;
import com.primebuild_online.repository.ItemRepository;
import com.primebuild_online.service.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ManufacturerService manufacturerService;
    private final FeatureService featureService;
    private final ItemFeatureService itemFeatureService;
    private final ComponentService componentService;
    private final ManufacturerItemService manufacturerItemService;

    public ItemServiceImpl(ComponentService componentService,
                           @Lazy FeatureService featureService,
                           @Lazy ItemFeatureService itemFeatureService,
                           ItemRepository itemRepository,
                           ManufacturerItemService manufacturerItemService,
                           ManufacturerService manufacturerService) {
        this.componentService = componentService;
        this.featureService = featureService;
        this.itemFeatureService = itemFeatureService;
        this.itemRepository = itemRepository;
        this.manufacturerItemService = manufacturerItemService;
        this.manufacturerService = manufacturerService;
    }

    @Override
    public Item saveItemReq(ItemReqDTO itemReqDTO) {
        Item newItem = new Item();

        newItem = itemSetValues(itemReqDTO, newItem);

        return itemRepository.save(newItem);
    }

    //    SRP Violated  manufacturerItemService.saveManufacturerItem(...)
    public Item itemSetValues(ItemReqDTO itemReqDTO, Item item) {
        item.setItemName(itemReqDTO.getItemName());
        item.setQuantity(itemReqDTO.getQuantity());
        item.setPrice(itemReqDTO.getPrice());
        item.setPowerConsumption(itemReqDTO.getPowerConsumption());
        item.setDiscountPercentage(itemReqDTO.getDiscountPercentage());

        if (itemReqDTO.getComponentId() != null) {
            Component component = componentService.getComponentById(itemReqDTO.getComponentId());
            item.setComponent(component);
        }

        Item savedItem = itemRepository.save(item);

        if (itemReqDTO.getManufacturerId() != null) {
            Manufacturer manufacturer = manufacturerService.getManufacturerById(itemReqDTO.getManufacturerId());
            item.setManufacturer(manufacturer);

            ManufacturerItem manufacturerItem = new ManufacturerItem();
            manufacturerItem.setItem(savedItem);
            manufacturerItem.setManufacturer(manufacturer);

            manufacturerItemService.saveManufacturerItem(manufacturerItem);
        }
        return savedItem;
    }


    //    SRP Violated by itemFeatureService.saveItemFeature(...)
    private void saveNewItemFeatures(Item item, List<Feature> featureList) {
        if (featureList != null && !featureList.isEmpty()) {
            itemFeatureService.deleteAllByItemId(item.getId());
            for (Feature featureRequest : featureList) {
                Feature feature = featureService.getFeatureById(featureRequest.getId());
                ItemFeature itemFeature = new ItemFeature();
                itemFeature.setItem(item);
                itemFeature.setFeature(feature);
                itemFeatureService.saveItemFeature(itemFeature);
            }
        }
    }

    @Override
    public List<Item> getAllItem() {
        return itemRepository.findAll();
    }

    @Override
    public Item updateItemReq(ItemReqDTO itemReqDTO, Long id) {
        Item itemInDb = itemRepository.findById(id).orElseThrow(RuntimeException::new);
        itemInDb = itemSetValues(itemReqDTO, itemInDb);

        return itemRepository.save(itemInDb);
    }

    @Override
    public Item getItemById(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new RuntimeException("Item not found");
        }
    }

    @Override
    public void deleteItem(Long id) {
        itemFeatureService.deleteAllByItemId(id);
        itemRepository.deleteById(id);
    }

    @Override
    public void saveItem(Item itemByBuildItem) {
        itemRepository.save(itemByBuildItem);
    }

    @Override
    public List<Item> getInStockItemListByComponent(Long componentId) {
        return itemRepository.findByQuantityGreaterThanAndComponentId(0, componentId);
    }

    @Override
    public List<Item> getItemsByIds(List<Long> ids) {
        return itemRepository.findAllById(ids);
    }

    @Override
    public void reduceItemQuantity(Item itemInDb, Integer quantityToReduce) {
        Integer reduceQuantity = itemInDb.getQuantity() - quantityToReduce;
        itemInDb.setQuantity(reduceQuantity);
        itemRepository.save(itemInDb);
    }

    @Override
    public void resetStock(Item item, Integer quantityToAdd) {
        item.setQuantity(item.getQuantity() + quantityToAdd);
        itemRepository.save(item);
    }


}
