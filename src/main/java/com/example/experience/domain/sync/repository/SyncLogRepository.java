package com.example.experience.domain.sync.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.experience.domain.sync.entity.SyncLog;

public interface SyncLogRepository extends JpaRepository<SyncLog, String>, JpaSpecificationExecutor<SyncLog> {
    List<SyncLog> findByDataSourceId(String dataSourceId);

    List<SyncLog> findByCreatedBy(String createdBy);

    Optional<SyncLog> findByIdAndCreatedBy(String id, String createdBy);
}
