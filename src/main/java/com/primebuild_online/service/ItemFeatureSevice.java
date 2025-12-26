package com.primebuild_online.service;

import com.primebuild_online.model.ItemFeature;

import java.util.List;

public interface ItemFeatureSevice {
    ItemFeature saveItemFeature(ItemFeature itemFeature);

    List<ItemFeature> getAllItemFeature();

    ItemFeature updateItemFeature(ItemFeature itemFeature, long id);

    ItemFeature getItemFeatureById(long id);

    void deleteItemFeature(long id);

    List<ItemFeature> findByItemId(Long itemId);

}
