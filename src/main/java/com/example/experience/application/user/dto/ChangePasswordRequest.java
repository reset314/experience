package com.example.experience.application.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChangePasswordRequest(
        @NotBlank String userId,
        @NotBlank String oldPassword,
        @NotBlank String password
) {
}
