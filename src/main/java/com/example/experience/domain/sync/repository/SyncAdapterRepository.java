package com.example.experience.domain.sync.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.experience.domain.sync.entity.SyncAdapter;

public interface SyncAdapterRepository extends JpaRepository<SyncAdapter, String>, JpaSpecificationExecutor<SyncAdapter> {
    Optional<SyncAdapter> findByAdapterKey(String adapterKey);

    List<SyncAdapter> findByCreatedBy(String createdBy);

    Optional<SyncAdapter> findByAdapterKeyAndCreatedBy(String adapterKey, String createdBy);

    Optional<SyncAdapter> findByIdAndCreatedBy(String id, String createdBy);
}
