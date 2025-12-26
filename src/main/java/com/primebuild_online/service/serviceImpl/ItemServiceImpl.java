package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.Feature;
import com.primebuild_online.model.Item;
import com.primebuild_online.repository.ItemRepository;
import com.primebuild_online.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Item saveItem(Item item){
        item.setComponent(item.getComponent());
        return itemRepository.save(item);
    }

    @Override
    public List<Item> getAllItem() {
        return itemRepository.findAll();
    }

    @Override
    public Item updateItem(Item item, long id) {
       Item existingItem = itemRepository.findById(id).orElseThrow(RuntimeException::new);
       existingItem.setItemName(item.getItemName());
       existingItem.setComponent(item.getComponent());
       existingItem.setStockQuantity(item.getStockQuantity());
       itemRepository.save(existingItem);
       return existingItem;
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
    public List<Feature> getItemFeature(Item item) {
        List<Feature> featureList = new ArrayList<>();
        Component component = item.getComponent();
//        featureList = item.getComponent().getComponentFeatureList();
        return null;
    }
}
