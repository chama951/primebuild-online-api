package com.primebuild_online.repository;

import com.primebuild_online.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeatureRepository extends JpaRepository<Feature, Long> {

    List<Feature> findAllByFeatureTypeId(Long featureTypeId);

    boolean existsByFeatureNameIgnoreCaseAndFeatureTypeId(String featureName, Long id);
}
