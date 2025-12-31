package com.primebuild_online.repository;

import com.primebuild_online.model.ComponentFeatureType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentFeatureRepository  extends JpaRepository<ComponentFeatureType, Long> {
}
