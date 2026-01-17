package com.primebuild_online.service;

import com.primebuild_online.model.DTO.ItemFeatureDTO;
import com.primebuild_online.model.ItemFeature;

import java.util.List;

public interface ItemFeatureService {
    ItemFeature saveItemFeatureRequest(ItemFeatureDTO itemFeatureDTO);

    List<ItemFeature> getAllItemFeature();

    List<ItemFeature> getItemFeatureByItem(Long itemId);

    ItemFeature updateItemFeatureRequest(ItemFeatureDTO itemFeatureDTO, Long id);

    ItemFeature getItemFeatureById(Long id);

    void deleteAllByItemId(Long itemId);

    void deleteItemFeature(Long id);

    List<ItemFeature> findByItemId(Long itemId);

    void saveItemFeature(ItemFeature itemFeature);

    void deleteAllByFeatureId(Long id);
}
