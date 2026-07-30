package com.example.experience.application.event.dto;

import java.time.Instant;

public record CreateEventRequest(
    String deviceMac,
    String platform,
    String eventType,
    Instant occurredAt,
    String payload,
    String sourceType,
    String operatorId,
    String targetId
) {
}
