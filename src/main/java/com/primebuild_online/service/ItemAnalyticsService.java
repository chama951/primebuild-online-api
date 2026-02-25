package com.primebuild_online.service;

import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemAnalytics;

import java.util.List;

public interface ItemAnalyticsService {
    void saveItemAnalytics(Item item);

    void atReduceItemQuantity(Item itemInDb, Integer quantityToReduce);

    void atResetItemQuantity(Item item, Integer quantityToAdd);

    boolean existsItemAnalyticsByItem(Long id);

    void atAddItemToCart(Item itemInDb, Integer quantity);

    void atRemoveItemFromCart(Item item, Integer cartQuantity);

    void incrementView(Long itemId);

    List<ItemAnalytics> getAllCartCounts();

    List<ItemAnalytics> getAllSalesCounts();

    List<ItemAnalytics> getAllViewCounts();

    List<ItemAnalytics> getAllItemAnalyticsByTrendScore();
}
