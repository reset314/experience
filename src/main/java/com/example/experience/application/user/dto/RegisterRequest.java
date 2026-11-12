package com.example.experience.application.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String password) {
}
