package com.primebuild_online.repository;

import com.primebuild_online.model.ItemFeature;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ItemFeatureRepository extends JpaRepository<ItemFeature, Long> {
    List<ItemFeature> findByItemId(Long itemId);

    @Transactional
    @Modifying
    void deleteByItemId(Long itemId);
}
