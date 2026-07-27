package com.example.experience.domain.rbac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.experience.domain.rbac.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(String name);
}
