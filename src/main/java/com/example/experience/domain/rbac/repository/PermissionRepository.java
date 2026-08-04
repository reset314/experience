package com.example.experience.domain.rbac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.experience.domain.rbac.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String>, JpaSpecificationExecutor<Permission> {
    Optional<Permission> findByResourceAndAction(String resource, String action);
}
