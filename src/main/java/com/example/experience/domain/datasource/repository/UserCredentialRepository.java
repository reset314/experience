package com.example.experience.domain.datasource.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.experience.domain.datasource.entity.UserCredential;

public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {
    List<UserCredential> findByUserId(String userId);

    List<UserCredential> findByDataSourceId(String dataSourceId);

    List<UserCredential> findByCreatedBy(String createdBy);

    Optional<UserCredential> findByIdAndCreatedBy(String id, String createdBy);
}
