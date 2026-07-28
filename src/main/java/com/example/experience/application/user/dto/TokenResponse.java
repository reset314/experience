package com.example.experience.application.user.dto;

public record TokenResponse(
    String userId,
    String username,
    String accessToken,
    String refreshToken,
    long expiresIn
) {
}
