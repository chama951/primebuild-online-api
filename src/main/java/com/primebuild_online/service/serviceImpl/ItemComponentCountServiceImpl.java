package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.DTO.ItemComponentCountDTO;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemComponentCount;
import com.primebuild_online.repository.ItemComponentCountRepository;
import com.primebuild_online.service.ComponentService;
import com.primebuild_online.service.ItemComponentCountService;
import com.primebuild_online.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemComponentCountServiceImpl implements ItemComponentCountService {

    @Autowired
    private final ComponentService componentService;

    @Autowired

    private final ItemService itemService;
    @Autowired

    private final ItemComponentCountRepository itemComponentCountRepository;

    public ItemComponentCountServiceImpl(ComponentService componentService, ItemService itemService, ItemComponentCountRepository itemComponentCountRepository) {
        this.componentService = componentService;
        this.itemService = itemService;
        this.itemComponentCountRepository = itemComponentCountRepository;
    }

    ItemComponentCount itemComponentCount = new ItemComponentCount();

    @Override
    public ItemComponentCount saveItemComponentCount(ItemComponentCountDTO itemComponentCountDTO) {
        if (isGetByItemAndComponent(itemComponentCountDTO.getItemId(), itemComponentCountDTO.getComponentId())) {
            throw new RuntimeException("Already Exist by Item and Component");
        }
        ItemComponentCount itemComponentCount = setValues(itemComponentCountDTO);
        return itemComponentCountRepository.save(itemComponentCount);
    }

    private ItemComponentCount setValues(ItemComponentCountDTO itemComponentCountDTO) {
        if (itemComponentCountDTO.getComponentId() != null) {
            Component componentInDb = componentService.getComponentById(itemComponentCountDTO.getComponentId());
            itemComponentCount.setComponent(componentInDb);
        }
        if (itemComponentCountDTO.getItemId() != null) {
            Item itemInDb = itemService.getItemById(itemComponentCountDTO.getItemId());
            itemComponentCount.setItem(itemInDb);
        }
        itemComponentCount.setComponentCount(itemComponentCountDTO.getComponentCount());
        return itemComponentCount;
    }

    private ItemComponentCount setValues(ItemComponentCount itemComponentCount, ItemComponentCountDTO itemComponentCountDTO, long id) {
        if (itemComponentCountDTO.getComponentId() != null) {
            Component componentInDb = componentService.getComponentById(itemComponentCountDTO.getComponentId());
            itemComponentCount.setComponent(componentInDb);
        }
        if (itemComponentCountDTO.getItemId() != null) {
            Item itemInDb = itemService.getItemById(itemComponentCountDTO.getItemId());
            itemComponentCount.setItem(itemInDb);
        }
        itemComponentCount.setComponentCount(itemComponentCountDTO.getComponentCount());
        return itemComponentCount;
    }

    @Override
    public ItemComponentCount updateItemComponentCount(ItemComponentCountDTO itemComponentCountDTO, long id) {
        ItemComponentCount itemComponentCountInDb = itemComponentCountRepository.findById(id).orElseThrow(RuntimeException::new);
        return itemComponentCountRepository.save(setValues(itemComponentCountInDb, itemComponentCountDTO, id));
    }

    @Override
    public ItemComponentCount getItemComponentCountById(long id) {
        Optional<ItemComponentCount> itemComponentCountInDb = itemComponentCountRepository.findById(id);
        if (itemComponentCountInDb.isPresent()) {
            return itemComponentCountInDb.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public List<ItemComponentCount> getAllItemComponentCount() {
        return itemComponentCountRepository.findAll();
    }

    @Override
    public void deleteItemComponentCount(long id) {
        itemComponentCountRepository.deleteById(id);
    }

    @Override
    public List<ItemComponentCount> getByItem(Long itemId) {
        return itemComponentCountRepository.findAllByItemId(itemId);
    }

    public boolean isGetByItemAndComponent(Long itemId, Long componentId) {
        return itemComponentCountRepository.findByItemIdAndComponentId(itemId, componentId).isPresent();
    }

    @Override
    public Optional<ItemComponentCount> getByItemAndComponent(Long itemId, Long componentId) {
        return itemComponentCountRepository.findByItemIdAndComponentId(itemId, componentId);
    }
}
