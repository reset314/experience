package com.example.experience.domain.user.repository;

import com.example.experience.domain.user.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, String>, JpaSpecificationExecutor<UserSession> {

    Optional<UserSession> findByRefreshTokenHash(String refreshTokenHash);

    Optional<UserSession> findByPreviousTokenHash(String previousTokenHash);

    List<UserSession> findByUserIdAndStatus(String userId, String status);

    List<UserSession> findByUserId(String userId);
}
