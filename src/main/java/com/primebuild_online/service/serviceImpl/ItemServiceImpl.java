package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.*;
import com.primebuild_online.model.DTO.ItemReqDTO;
import com.primebuild_online.repository.ItemRepository;
import com.primebuild_online.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private FeatureService featureService;
    @Autowired
    private ItemFeatureService itemFeatureService;
    @Autowired
    private ComponentService componentService;
    @Autowired
    private ManufacturerItemService manufacturerItemService;

    @Autowired
    public void setItemFeatureService(@Lazy ItemFeatureService itemFeatureService) {
        this.itemFeatureService = itemFeatureService;
    }

    @Override
    public Item saveItemReq(ItemReqDTO itemReqDTO) {
        Item newItem = new Item();

        newItem = itemSetValues(itemReqDTO, newItem);

        return itemRepository.save(newItem);
    }

    public Item itemSetValues(ItemReqDTO itemReqDTO, Item item) {
        item.setItemName(itemReqDTO.getItemName());
        item.setQuantity(itemReqDTO.getQuantity());
        item.setPrice(itemReqDTO.getPrice());

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
        saveNewItemFeatures(savedItem, itemReqDTO.getFeatureList());

        return savedItem;
    }


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
    public Item updateItemReq(ItemReqDTO itemReqDTO, long id) {
        Item itemInDb = itemRepository.findById(id).orElseThrow(RuntimeException::new);
        itemInDb = itemSetValues(itemReqDTO, itemInDb);

        return itemRepository.save(itemInDb);
    }

    @Override
    public Item getItemById(long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteItem(long id) {
        itemRepository.findById(id).orElseThrow(RuntimeException::new);
        itemRepository.deleteById(id);
    }

    @Override
    public void saveItem(Item itemByBuildItem) {
        itemRepository.save(itemByBuildItem);
    }

    @Override
    public List<Item> getInStockItemListByComponent(long componentId) {
        return itemRepository.findByQuantityGreaterThanAndComponentId(0, componentId);
    }

}
