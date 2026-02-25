package com.primebuild_online.repository;

import com.primebuild_online.model.FeatureType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureTypeRepository extends JpaRepository<FeatureType, Long> {

    boolean existsByFeatureTypeNameIgnoreCaseAndIdNot(String featureTypeName, Long id);

    boolean existsByFeatureTypeNameIgnoreCase(String featureTypeName);
}
