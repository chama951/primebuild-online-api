package com.primebuild_online.repository;

import com.primebuild_online.model.ItemData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDataRepository extends JpaRepository<ItemData, Long> {
}
