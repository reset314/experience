package com.example.experience.application.user.service.impl;

import com.example.experience.application.user.dto.DeviceInfo;
import com.example.experience.application.user.dto.RefreshRequest;
import com.example.experience.application.user.dto.TokenResponse;
import com.example.experience.application.user.service.AuthenticationService;
import com.example.experience.application.user.service.JwtService;
import com.example.experience.application.user.service.UserSessionService;
import com.example.experience.domain.rbac.Role;
import com.example.experience.domain.rbac.entity.UserRole;
import com.example.experience.domain.rbac.repository.UserRoleRepository;
import com.example.experience.domain.user.entity.RevokeReason;
import com.example.experience.domain.user.entity.User;
import com.example.experience.domain.user.entity.UserSession;
import com.example.experience.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserSessionService userSessionService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public TokenResponse login(String username, String rawPassword, DeviceInfo deviceInfo) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        if (!"active".equals(user.getStatus())) {
            throw new IllegalArgumentException("User account is not active");
        }

        user.updateLastLoginAt(Instant.now());
        userRepository.save(user);

        List<Role> roles = loadRoles(user.getId());
        UserSession session = userSessionService.createSession(user, deviceInfo);
        String accessToken = jwtService.generateAccessToken(user, roles);

        return new TokenResponse(
            user.getId(),
            user.getUsername(),
            accessToken,
            session.getPlainRefreshToken(),
            jwtService.getAccessTokenExpirationSeconds()
        );
    }

    @Override
    @Transactional
    public TokenResponse refresh(RefreshRequest request, DeviceInfo deviceInfo) {
        String hash = userSessionService.hashRefreshToken(request.refreshToken());
        Optional<UserSession> optionalSession = userSessionService.findActiveByRefreshTokenHash(hash);

        if (optionalSession.isEmpty()) {
            userSessionService.findByPreviousTokenHash(hash).ifPresent(session ->
                userSessionService.revokeAllByUser(
                    session.getUser().getId(), RevokeReason.REUSE_DETECTED));
            throw new IllegalArgumentException("Invalid or revoked refresh token");
        }

        UserSession oldSession = optionalSession.get();
        User user = oldSession.getUser();
        UserSession newSession = userSessionService.rotateSession(oldSession);
        List<Role> roles = loadRoles(user.getId());
        String accessToken = jwtService.generateAccessToken(user, roles);

        return new TokenResponse(
            user.getId(),
            user.getUsername(),
            accessToken,
            newSession.getPlainRefreshToken(),
            jwtService.getAccessTokenExpirationSeconds()
        );
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        String hash = userSessionService.hashRefreshToken(refreshToken);
        userSessionService.findActiveByRefreshTokenHash(hash)
            .ifPresent(session -> userSessionService.revokeSession(session, RevokeReason.LOGOUT));
    }

    @Override
    @Transactional
    public void logoutAll(String userId) {
        userSessionService.revokeAllByUser(userId, RevokeReason.LOGOUT_ALL);
    }

    private List<Role> loadRoles(String userId) {
        return userRoleRepository.findByUserId(userId).stream()
            .map(UserRole::getRole)
            .toList();
    }
}
