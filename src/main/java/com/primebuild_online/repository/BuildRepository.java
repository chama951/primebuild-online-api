package com.primebuild_online.repository;

import com.primebuild_online.model.Build;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildRepository extends JpaRepository<Build, Long> {
}
