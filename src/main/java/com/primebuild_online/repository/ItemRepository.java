package com.primebuild_online.repository;

import com.primebuild_online.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByQuantityGreaterThanAndComponentId(int i, Long componentId);

    Page<Item> findByQuantityGreaterThanAndComponentId(int i, Long componentId,Pageable pageable);

    List<Item> findAllById(Iterable<Long> ids);

    boolean existsByItemNameIgnoreCaseAndComponentId(String itemName, Long id);

    boolean existsByManufacturer_Id(Long id);

    boolean existsByComponent_Id(Long componentId);

    Optional<Item> getItemsById(Long id);

    Page<Item> findByItemNameContainingIgnoreCase(String search, Pageable pageable);
}
