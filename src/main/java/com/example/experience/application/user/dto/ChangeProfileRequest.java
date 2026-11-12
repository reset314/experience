package com.example.experience.application.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import tools.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChangeProfileRequest(
        @NotBlank String userId,
        @NotBlank String avatarUri,
        @NotBlank String displayName,
        @NotBlank JsonNode settings
        ) {
}
