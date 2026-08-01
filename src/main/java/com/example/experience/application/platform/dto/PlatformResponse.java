package com.example.experience.application.platform.dto;

import java.time.Instant;

public record PlatformResponse(
    String id,
    String name,
    String displayName,
    String description,
    String adapterId,
    String metadata,
    String config,
    String visibility,
    String icon,
    String createdBy,
    Instant updatedAt
) {

}
