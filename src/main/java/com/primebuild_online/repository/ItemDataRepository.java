package com.primebuild_online.repository;

import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemData;
import com.primebuild_online.model.enumerations.Vendors;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemDataRepository extends JpaRepository<ItemData, Long> {
    Optional<ItemData> findTopByItemAndVendorOrderByRecordedAtDesc(Item item, Vendors vendors);
}
