package com.primebuild_online.repository;

import com.primebuild_online.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByQuantityGreaterThanAndComponentId(int i, Long componentId);

    List<Item> findAllById(Iterable<Long> ids);

    boolean existsByItemNameIgnoreCaseAndComponentId(String itemName, Long id);

    boolean existsByManufacturer_Id(Long id);

    boolean existsByComponent_Id(Long componentId);

    Optional<Item> getItemsById(Long id);
}
