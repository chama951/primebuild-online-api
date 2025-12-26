package com.primebuild_online.repository;

import com.primebuild_online.model.ItemFeature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemFeatureRepository extends JpaRepository<ItemFeature, Long> {
    List<ItemFeature> findByItemId(Long itemId);
}
