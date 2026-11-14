package com.example.experience.application.user.service;

import com.example.experience.application.user.dto.DeviceInfo;
import com.example.experience.domain.user.entity.RevokeReason;
import com.example.experience.domain.user.entity.User;
import com.example.experience.domain.user.entity.UserSession;

import java.util.Optional;

public interface UserSessionService {

    UserSession createSession(User user, DeviceInfo deviceInfo);

    Optional<UserSession> findActiveByRefreshTokenHash(String refreshTokenHash);

    Optional<UserSession> findByRefreshTokenHash(String refreshTokenHash);

    Optional<UserSession> findByPreviousTokenHash(String previousTokenHash);

    UserSession rotateSession(UserSession session);

    void revokeSession(UserSession session, RevokeReason reason);

    void revokeAllByUser(String userId, RevokeReason reason);

    void revokeAllByUserExcept(String userId, String exceptSessionId, RevokeReason reason);

    String hashRefreshToken(String refreshToken);
}
