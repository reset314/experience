package com.example.experience.application.user.dto;

import java.util.List;

public record JwtClaims(String userId, String username, List<String> roles) {
}
