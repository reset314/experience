package com.example.experience.application.user.dto;

import com.example.experience.domain.user.entity.SessionStatu;

import java.time.Instant;

public record SessionResponse(
        String id,
        SessionStatu statu,
        String deviceName,
        String ipAddress,
        String userAgent,
        Instant issuedAt,
        Instant lastUsedAt,
        Instant expiresAt
) {
}
