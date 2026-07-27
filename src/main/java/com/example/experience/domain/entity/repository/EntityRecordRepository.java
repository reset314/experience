package com.example.experience.domain.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.experience.domain.entity.entity.EntityRecord;

public interface EntityRecordRepository extends JpaRepository<EntityRecord, String> {
}
