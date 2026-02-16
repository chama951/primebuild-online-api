package com.primebuild_online.repository;

import com.primebuild_online.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByQuantityGreaterThanAndComponentId(int i, Long componentId);

    List<Item> findAllById(Iterable<Long> ids);
}
