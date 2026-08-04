package com.example.experience.domain.datasource.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.experience.domain.datasource.entity.UserCredential;

public interface UserCredentialRepository extends JpaRepository<UserCredential, String>, JpaSpecificationExecutor<UserCredential> {
    List<UserCredential> findByUserId(String userId);

    List<UserCredential> findByDataSourceId(String dataSourceId);

    List<UserCredential> findByCreatedBy(String createdBy);

    Optional<UserCredential> findByIdAndCreatedBy(String id, String createdBy);
}
