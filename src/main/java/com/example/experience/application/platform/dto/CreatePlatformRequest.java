package com.example.experience.application.platform.dto;

public record CreatePlatformRequest(
    String userId,
    String name,
    String displayName,
    String description,
    String adapterId,
    String metadata,
    String config,
    String visibility
) {

}
