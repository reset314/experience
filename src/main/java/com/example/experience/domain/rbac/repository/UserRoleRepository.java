package com.example.experience.domain.rbac.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.experience.domain.rbac.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    List<UserRole> findByUserId(String userId);
}