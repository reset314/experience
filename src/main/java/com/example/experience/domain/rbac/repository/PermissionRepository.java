package com.example.experience.domain.rbac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.experience.domain.rbac.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {
    Optional<Permission> findByResourceAndAction(String resource, String action);
}
