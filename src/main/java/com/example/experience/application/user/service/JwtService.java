package com.example.experience.application.user.service;

import com.example.experience.application.user.dto.JwtClaims;
import com.example.experience.domain.rbac.Role;
import com.example.experience.domain.user.entity.User;

import java.util.List;

public interface JwtService {

    String generateAccessToken(User user, List<Role> roles);

    JwtClaims validateAccessToken(String token);

    long getAccessTokenExpirationSeconds();
}
