package com.primebuild_online.service;

import com.primebuild_online.model.DTO.ItemReqDTO;
import com.primebuild_online.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ItemService {
    Item saveItemReq(ItemReqDTO itemReqDTO);

    Page<Item> getPaginatedAllItem(Pageable pageable);

    Item updateItemReq(ItemReqDTO itemReqDTO, Long id);

    Item getItemById(Long id);

    void deleteItem(Long id);

    Page<Item> getPaginatedInStockItemListByComponent(Long componentId, Pageable pageable);

    List<Item> getInStockItemListByComponentForCompatibility(Long componentId);

    List<Item> getItemsByIds(List<Long> ids);

    void reduceItemQuantity(Item item, Integer itemQuantity);

    void resetItemStockQuantity(Item item, Integer quantityToAdd);

    void checkItemsListStockQuantity(List<Item> itemList);

    void checkItemsStockQuantity(Item itemInDb, Item itemToAdd);

    BigDecimal calculateDiscountSubTotal(Item itemInDb, int quantity);

    BigDecimal calculateSubTotal(Item itemInDb, int quantity);

    BigDecimal calculateDiscountPerUnite(Item itemInDb);

    boolean existsItemByManufacturer(Long id);

    boolean existsItemByComponent(Long id);

    Page<Item> searchPaginatedItemsByName(String search, Pageable pageable);

    List<Item> getItemList();
}
