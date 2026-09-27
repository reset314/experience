package com.example.experience.application.user.dto;

public record UserProfileResponse(
    String userId,
    String displayName,
    boolean isIdVerified,
    String avatarData
) {

}
