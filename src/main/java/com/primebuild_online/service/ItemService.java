package com.primebuild_online.service;

import com.primebuild_online.model.DTO.ItemReqDTO;
import com.primebuild_online.model.Item;

import java.util.List;

public interface ItemService {
    Item saveItemReq(ItemReqDTO itemReqDTO);

    List<Item> getAllItem();


    Item updateItemReq(ItemReqDTO itemReqDTO, Long id);

    Item getItemById(Long id);

    void deleteItem(Long id);

    void saveItem(Item itemByBuildItem);

    List<Item> getInStockItemListByComponent(Long componentId);

    List<Item> getItemsByIds(List<Long> ids);
}
