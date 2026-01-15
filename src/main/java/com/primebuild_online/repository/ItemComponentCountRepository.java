package com.primebuild_online.repository;

import com.primebuild_online.model.ItemComponentCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemComponentCountRepository extends JpaRepository<ItemComponentCount,Long> {
    List<ItemComponentCount> findAllByItemId(Long itemId);

    Optional<ItemComponentCount> findByItemIdAndComponentId(Long itemId, Long componentId);

}
