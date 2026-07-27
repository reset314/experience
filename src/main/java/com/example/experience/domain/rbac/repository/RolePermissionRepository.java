package com.example.experience.domain.rbac.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.experience.domain.rbac.entity.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, String> {
    List<RolePermission> findByRoleId(String roleId);
}