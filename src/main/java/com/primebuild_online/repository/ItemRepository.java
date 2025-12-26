package com.primebuild_online.repository;

import com.primebuild_online.model.FeatureType;
import com.primebuild_online.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
