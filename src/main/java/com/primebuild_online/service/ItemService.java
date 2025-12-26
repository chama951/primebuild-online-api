package com.primebuild_online.service;

import com.primebuild_online.model.Feature;
import com.primebuild_online.model.Item;

import java.util.List;

public interface ItemService {
    Item saveItem(Item item);

    List<Item> getAllItem();

    Item updateItem(Item item, long id);

    Item getItemById(long id);

    void deleteItem(long id);

    List<Feature> getItemFeature(Item item);
}
