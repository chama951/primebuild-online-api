package com.primebuild_online.service;

import com.primebuild_online.model.DTO.ItemRequestDTO;
import com.primebuild_online.model.Item;

import java.util.List;

public interface ItemService {
    Item saveItemRequest(ItemRequestDTO itemRequestDTO);

    List<Item> getAllItem();

    Item updateItem(Item item, long id);

    Item updateItemRequest(ItemRequestDTO itemRequestDTO, long id);

    Item getItemById(long id);

    void deleteItem(long id);

    void saveItem(Item itemByBuildItem);
}
