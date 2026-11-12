package com.example.experience.application.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RefreshRequest(
        @NotBlank String refreshToken
) {
}
