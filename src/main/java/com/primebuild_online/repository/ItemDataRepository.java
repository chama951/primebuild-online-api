package com.primebuild_online.repository;

import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemData;
import com.primebuild_online.model.enumerations.Vendors;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ItemDataRepository extends JpaRepository<ItemData, Long> {

    List<ItemData> findByVendorAndItem_id(Vendors vendor, Long id);

    Optional<ItemData> findByVendorAndItemAndVendorPriceAndOurPrice(Vendors vendor, Item item, BigDecimal bigDecimal, BigDecimal price);

    List<ItemData> findAllByItem_Id(Long id);

    Optional<ItemData> findByVendorAndItemAndVendorPrice(Vendors vendor, Item item, BigDecimal bigDecimal);
}
