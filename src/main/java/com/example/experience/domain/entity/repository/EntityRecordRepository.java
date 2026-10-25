package com.example.experience.domain.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.experience.domain.entity.entity.EntityRecord;

public interface EntityRecordRepository extends JpaRepository<EntityRecord, String>, JpaSpecificationExecutor<EntityRecord> {

    List<EntityRecord> findByUserIdAndType(String userId, String type);

    List<EntityRecord> findByUserIdAndPlatformAndType(String userId, String platform, String type);

    Optional<EntityRecord> findByIdAndUserId(String id, String userId);
}
