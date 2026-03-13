package com.primebuild_online.service;

import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemAnalytics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemAnalyticsService {
    void saveItemAnalytics(Item item);

    void atReduceItemQuantity(Item itemInDb, Integer quantityToReduce);

    void atResetItemQuantity(Item item, Integer quantityToAdd);

    boolean existsItemAnalyticsByItem(Long id);

    void atAddItemToCart(Item itemInDb, Integer quantity);

    void atRemoveItemFromCart(Item item, Integer cartQuantity);

    void incrementView(Long itemId);

    Page<ItemAnalytics> getAllCartCounts(Pageable pageable);

    Page<ItemAnalytics> getAllSalesCounts(Pageable pageable);

    Page<ItemAnalytics> getAllViewCounts(Pageable pageable);

    Page<ItemAnalytics> getAllItemAnalyticsByTrendScore(Pageable pageable);
}
