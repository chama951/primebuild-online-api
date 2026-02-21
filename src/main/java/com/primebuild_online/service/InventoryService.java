package com.primebuild_online.service;

import com.primebuild_online.model.Item;

public interface InventoryService {
    void checkLowStock(Item item);
}
