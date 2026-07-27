package com.example.experience.domain.sync.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.experience.domain.sync.entity.SyncAdapter;

public interface SyncAdapterRepository extends JpaRepository<SyncAdapter, String> {
    Optional<SyncAdapter> findByAdapterKey(String adapterKey);
}
