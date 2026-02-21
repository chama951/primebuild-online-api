package com.primebuild_online.service;

import com.primebuild_online.model.DTO.ItemReqDTO;
import com.primebuild_online.model.Item;

import java.math.BigDecimal;
import java.util.List;

public interface ItemService {
    Item saveItemReq(ItemReqDTO itemReqDTO);

    List<Item> getAllItem();

    Item updateItemReq(ItemReqDTO itemReqDTO, Long id);

    Item getItemById(Long id);

    void deleteItem(Long id);

    List<Item> getInStockItemListByComponent(Long componentId);

    List<Item> getItemsByIds(List<Long> ids);

    void reduceItemQuantity(Item item, Integer itemQuantity);

    void resetItemStockQuantity(Item item, Integer quantityToAdd);

    void checkItemsListStockQuantity(List<Item> itemList);

    void checkItemsStockQuantity(Item itemInDb, Item itemToAdd);

    BigDecimal calculateDiscountSubTotal(Item itemInDb, int quantity);

    BigDecimal calculateSubTotal(Item itemInDb, int quantity);

    BigDecimal calculateDiscountPerUnite(Item itemInDb);
}
