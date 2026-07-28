package com.example.experience.application.user.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.experience.application.user.dto.DeviceInfo;
import com.example.experience.application.user.service.UserSessionService;
import com.example.experience.common.utils.Uuid7Utils;
import com.example.experience.domain.user.entity.User;
import com.example.experience.domain.user.entity.UserSession;
import com.example.experience.domain.user.repository.UserSessionRepository;
import com.example.experience.infrastructure.config.AuthProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository userSessionRepository;
    private final AuthProperties authProperties;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    @Transactional
    public UserSession createSession(User user, DeviceInfo deviceInfo) {
        String plainRefreshToken = generateRefreshToken();
        String refreshTokenHash = hashRefreshToken(plainRefreshToken);

        Instant now = Instant.now();
        Instant expiresAt = now.plus(authProperties.refreshTokenExpiration());

        UserSession session = UserSession.create(
            Uuid7Utils.generateUuid7(),
            user,
            refreshTokenHash,
            now,
            expiresAt,
            deviceInfo != null ? deviceInfo.deviceName() : null,
            deviceInfo != null ? deviceInfo.ipAddress() : null,
            deviceInfo != null ? deviceInfo.userAgent() : null
        );
        session.setPlainRefreshToken(plainRefreshToken);

        return userSessionRepository.save(session);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserSession> findActiveByRefreshTokenHash(String refreshTokenHash) {
        return userSessionRepository.findByRefreshTokenHash(refreshTokenHash)
            .filter(UserSession::isActive);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserSession> findByRefreshTokenHash(String refreshTokenHash) {
        return userSessionRepository.findByRefreshTokenHash(refreshTokenHash);
    }

    @Override
    @Transactional
    public UserSession rotateSession(UserSession oldSession, DeviceInfo deviceInfo) {
        oldSession.revoke();
        userSessionRepository.save(oldSession);
        return createSession(oldSession.getUser(), deviceInfo);
    }

    @Override
    @Transactional
    public void revokeSession(UserSession session) {
        session.revoke();
        userSessionRepository.save(session);
    }

    @Override
    @Transactional
    public void revokeAllByUser(String userId) {
        userSessionRepository.findByUserIdAndStatus(userId, "active")
            .forEach(session -> {
                session.revoke();
                userSessionRepository.save(session);
            });
    }

    @Override
    @Transactional
    public void revokeAllByUserExcept(String userId, String exceptSessionId) {
        userSessionRepository.findByUserIdAndStatus(userId, "active").stream()
            .filter(session -> exceptSessionId == null || !session.getId().equals(exceptSessionId))
            .forEach(session -> {
                session.revoke();
                userSessionRepository.save(session);
            });
    }

    @Override
    public String hashRefreshToken(String refreshToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(refreshToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    private String generateRefreshToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
