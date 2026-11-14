package com.example.experience.application.user.dto;

import com.example.experience.domain.rbac.Role;

import java.util.List;

public record JwtClaims(String userId, String username, List<Role> roles) {
}
