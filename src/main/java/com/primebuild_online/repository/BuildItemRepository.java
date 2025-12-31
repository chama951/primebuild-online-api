package com.primebuild_online.repository;

import com.primebuild_online.model.BuildItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildItemRepository extends JpaRepository<BuildItem, Long> {
}
