package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.*;
import com.primebuild_online.model.DTO.ItemRequestDTO;
import com.primebuild_online.repository.ItemRepository;
import com.primebuild_online.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ItemFeatureSevice itemFeatureSevice;
    @Autowired
    private ComponentService componentService;
    @Autowired
    private ManufacturerItemService manufacturerItemService;

    @Override
    public Item saveItemRequest(ItemRequestDTO itemRequestDTO) {
        Item newItem = new Item();

        newItem = itemSetValues(itemRequestDTO, newItem);

        return itemRepository.save(newItem);
    }

    public Item itemSetValues(ItemRequestDTO itemRequestDTO, Item item) {
        item.setItemName(itemRequestDTO.getItemName());
        item.setQuantity(itemRequestDTO.getQuantity());
        item.setPrice(itemRequestDTO.getPrice());

        Component component = componentService.getComponentById(itemRequestDTO.getComponent().getId());
        item.setComponent(component);

        Manufacturer manufacturer = manufacturerService.getManufacturerById(itemRequestDTO.getManufacturer().getId());
        item.setManufacturer(manufacturer);

        Item savedItem = itemRepository.save(item);

        ManufacturerItem manufacturerItem = new ManufacturerItem();
        manufacturerItem.setItem(savedItem);
        manufacturerItem.setManufacturer(manufacturer);

        manufacturerItemService.saveManufacturerItem(manufacturerItem);

        saveNewItemFeatures(savedItem, itemRequestDTO.getFeatureList());

        return savedItem;
    }




    private void saveNewItemFeatures(Item item, List<Feature> featureList) {
        if (featureList != null && !featureList.isEmpty()) {
            itemFeatureSevice.deleteAllByItemId(item.getId());
            for (Feature featureRequest : featureList) {
                Feature feature = featureService.getFeatureById(featureRequest.getId());
                ItemFeature itemFeature = new ItemFeature();
                itemFeature.setItem(item);
                itemFeature.setFeature(feature);
                itemFeatureSevice.saveItemFeature(itemFeature);
            }
        }
    }

    @Override
    public List<Item> getAllItem() {
        return itemRepository.findAll();
    }

    @Override
    public Item updateItem(Item item, long id) {
        Item existingItem = itemRepository.findById(id).orElseThrow(RuntimeException::new);
        existingItem.setItemName(item.getItemName());
        existingItem.setManufacturer(item.getManufacturer());
        existingItem.setComponent(item.getComponent());
        existingItem.setQuantity(item.getQuantity());
        existingItem.setPrice(item.getPrice());
        itemRepository.save(existingItem);
        return existingItem;
    }

    @Override
    public Item updateItemRequest(ItemRequestDTO itemRequestDTO, long id) {
        Item itemInDb = itemRepository.findById(id).orElseThrow(RuntimeException::new);
        itemInDb = itemSetValues(itemRequestDTO, itemInDb);
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

}
