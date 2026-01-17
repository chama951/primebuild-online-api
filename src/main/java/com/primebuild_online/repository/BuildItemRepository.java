package com.primebuild_online.repository;

import com.primebuild_online.model.BuildItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface BuildItemRepository extends JpaRepository<BuildItem, Long> {

    Optional<BuildItem> findByBuildIdAndItemId(Long buildId, Long itemId);

    @Transactional
    @Modifying
    void deleteAllByBuildId(Long buildId);

    List<BuildItem> findAllByBuildId(Long buildId);
}
