package com.primebuild_online.repository;

import com.primebuild_online.model.ComponentFeatureType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ComponentFeatureTypeRepository extends JpaRepository<ComponentFeatureType, Long> {

    @Transactional
    @Modifying
    void deleteAllComponentFeatureTypeByComponentId(Long id);
}
