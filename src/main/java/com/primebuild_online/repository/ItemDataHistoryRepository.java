package com.primebuild_online.repository;

import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemDataHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemDataHistoryRepository extends JpaRepository<ItemDataHistory, Long> {
    List<ItemDataHistory> findAllByItemOrderByScrapedAtDesc(Item item);
}
