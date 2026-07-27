package com.example.experience.domain.datasource.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.experience.domain.datasource.entity.UserDataSource;

public interface UserDataSourceRepository extends JpaRepository<UserDataSource, String> {
    Optional<UserDataSource> findByUserIdAndPlatformNameAndSourceIdentifier(String userId, String platformName, String sourceIdentifier);
    List<UserDataSource> findByUserId(String userId);
}
