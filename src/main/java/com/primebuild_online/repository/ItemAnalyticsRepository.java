package com.primebuild_online.repository;

import com.primebuild_online.model.ItemAnalytics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemAnalyticsRepository extends JpaRepository<ItemAnalytics, Long> {
    ItemAnalytics getByItem_Id(Long itemId);

    boolean existsByItem_Id(Long id);

    Page<ItemAnalytics> findAllByOrderByTrendScoreDesc(Pageable pageable);

    Page<ItemAnalytics> findAllByOrderByTotalViewsDesc(Pageable pageable);

    Page<ItemAnalytics> findAllByOrderByTotalSalesDesc(Pageable pageable);

    Page<ItemAnalytics> findAllByOrderByTotalCartAddsDesc(Pageable pageable);
}
