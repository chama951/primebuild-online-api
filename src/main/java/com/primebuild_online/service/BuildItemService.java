package com.primebuild_online.service;

import com.primebuild_online.model.Build;
import com.primebuild_online.model.BuildItem;
import com.primebuild_online.model.Item;

import java.math.BigDecimal;
import java.util.List;

public interface BuildItemService{

    BuildItem saveBuildItem(Item itemToAdd, Build build);

    void updateBuildItem(BuildItem buildItem, Long id);

    BuildItem findByBuildIdAndItemId(Long buildId, Long itemId);

    void deleteAllByBuildId(Long buildId);

    void deleteById(Long id);

    List<BuildItem> findAllByBuildId(Long buildId);

    void resetItemQuantity(List<BuildItem> buildItemList);

    boolean existsBuildItemByItem(Long id);

    void updateBuildItemAtPriceChange(List<BuildItem> buildItemList);

    BigDecimal calculateDiscountAmount(List<BuildItem> buildItemList);

    BigDecimal calculateTotalAmount(List<BuildItem> buildItemList);

//    void updateBuildItemAtPriceChange(Long id);
}
