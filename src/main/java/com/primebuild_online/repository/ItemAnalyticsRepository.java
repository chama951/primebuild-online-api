package com.primebuild_online.repository;

import com.primebuild_online.model.ItemAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemAnalyticsRepository extends JpaRepository<ItemAnalytics, Long> {
    ItemAnalytics getByItem_Id(Long itemId);

    boolean existsByItem_Id(Long id);

    List<ItemAnalytics> findAllByOrderByTrendScoreDesc();

    List<ItemAnalytics> findAllByOrderByTotalViewsDesc();

    List<ItemAnalytics> findAllByOrderByTotalSalesDesc();

    List<ItemAnalytics> findAllByOrderByTotalCartAddsDesc();
}
