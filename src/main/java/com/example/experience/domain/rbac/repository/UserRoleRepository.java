package com.example.experience.domain.rbac.repository;

import com.example.experience.domain.rbac.entity.UserRole;
import com.example.experience.domain.rbac.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId>, JpaSpecificationExecutor<UserRole> {
    List<UserRole> findByUserId(String userId);

    List<UserRole> findByUserIdIn(Set<String> userIds);

    Optional<UserRole> findByUserIdAndIdRole(String userId, String role);

    void deleteByUserId(String userId);
}
