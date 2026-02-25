package com.primebuild_online.repository;

import com.primebuild_online.model.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComponentRepository extends JpaRepository<Component, Long> {
    List<Component> findAllByBuildComponent(boolean buildComponent);

    @Query("SELECT c FROM Component c " +
            "WHERE c.buildComponent = true " +
            "ORDER BY CASE WHEN c.buildPriority IS NULL " +
            "THEN 1 ELSE 0 END, c.buildPriority ASC")
    List<Component> findBuildComponentsOrderedByPriority();

    boolean existsByComponentNameIgnoreCase(String componentName);
}
