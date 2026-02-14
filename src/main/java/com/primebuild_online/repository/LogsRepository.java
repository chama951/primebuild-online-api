package com.primebuild_online.repository;

import com.primebuild_online.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogsRepository extends JpaRepository<Log, Long> {
}
