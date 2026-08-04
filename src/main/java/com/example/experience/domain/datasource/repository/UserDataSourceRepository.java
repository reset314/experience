package com.example.experience.domain.datasource.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.experience.domain.datasource.entity.UserDataSource;

public interface UserDataSourceRepository extends JpaRepository<UserDataSource, String>, JpaSpecificationExecutor<UserDataSource> {
    Optional<UserDataSource> findByUserIdAndPlatformNameAndSourceIdentifier(String userId, String platformName, String sourceIdentifier);

    List<UserDataSource> findByUserId(String userId);

    List<UserDataSource> findByCreatedBy(String createdBy);

    Optional<UserDataSource> findByIdAndCreatedBy(String id, String createdBy);
}
