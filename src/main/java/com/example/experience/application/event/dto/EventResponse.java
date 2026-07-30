package com.example.experience.application.event.dto;

import java.time.Instant;

public record EventResponse(
    String id,
    String createdBy,
    String userId,
    String operatorId,
    String targetId,
    String deviceMac,
    String platform,
    String eventType,
    Instant occurredAt,
    String sourceType,
    String description,
    Instant createdAt
) {
}
