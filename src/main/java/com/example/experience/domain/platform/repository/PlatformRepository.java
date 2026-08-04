package com.example.experience.domain.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.experience.domain.platform.entity.Platform;

public interface PlatformRepository extends JpaRepository<Platform, String>, JpaSpecificationExecutor<Platform> {
    Optional<Platform> findByNameAndCreatedBy(String name, String createdBy);

    List<Platform> findByCreatedBy(String createdBy);

    Optional<Platform> findByIdAndCreatedBy(String id, String createdBy);
}
