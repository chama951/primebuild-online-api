package com.primebuild_online.repository;

import com.primebuild_online.model.Component;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComponentRepository extends JpaRepository<Component, Long> {
    List<Component> findAllByBuildComponent(boolean buildComponent);
}
