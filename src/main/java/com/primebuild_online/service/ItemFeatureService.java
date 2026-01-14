package com.primebuild_online.service;

import com.primebuild_online.model.DTO.ItemFeatureDTO;
import com.primebuild_online.model.ItemFeature;

import java.util.List;

public interface ItemFeatureService {
    ItemFeature saveItemFeatureRequest(ItemFeatureDTO itemFeatureDTO);

    List<ItemFeature> getAllItemFeature();

    List<ItemFeature> getItemFeatureByItem(Long itemId);

    ItemFeature updateItemFeatureRequest(ItemFeatureDTO itemFeatureDTO, long id);

    ItemFeature getItemFeatureById(long id);

    void deleteAllByItemId(long itemId);

    void deleteItemFeature(long id);

    List<ItemFeature> findByItemId(Long itemId);

    void saveItemFeature(ItemFeature itemFeature);
}
