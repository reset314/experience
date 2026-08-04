package com.example.experience.domain.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.experience.domain.entity.entity.EntityRecord;

public interface EntityRecordRepository extends JpaRepository<EntityRecord, String>, JpaSpecificationExecutor<EntityRecord> {
    List<EntityRecord> findByCreatedBy(String createdBy);

    Optional<EntityRecord> findByIdAndCreatedBy(String id, String createdBy);
}
