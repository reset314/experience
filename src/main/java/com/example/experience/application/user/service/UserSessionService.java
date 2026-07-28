package com.example.experience.application.user.service;

import java.util.Optional;

import com.example.experience.application.user.dto.DeviceInfo;
import com.example.experience.domain.user.entity.User;
import com.example.experience.domain.user.entity.UserSession;

public interface UserSessionService {

    UserSession createSession(User user, DeviceInfo deviceInfo);

    Optional<UserSession> findActiveByRefreshTokenHash(String refreshTokenHash);

    Optional<UserSession> findByRefreshTokenHash(String refreshTokenHash);

    UserSession rotateSession(UserSession oldSession, DeviceInfo deviceInfo);

    void revokeSession(UserSession session);

    void revokeAllByUser(String userId);

    void revokeAllByUserExcept(String userId, String exceptSessionId);

    String hashRefreshToken(String refreshToken);
}
