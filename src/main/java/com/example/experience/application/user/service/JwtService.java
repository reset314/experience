package com.example.experience.application.user.service;

import java.util.List;

import com.example.experience.application.user.dto.JwtClaims;
import com.example.experience.domain.user.entity.User;

public interface JwtService {

    String generateAccessToken(User user, List<String> roles);

    JwtClaims validateAccessToken(String token);

    long getAccessTokenExpirationSeconds();
}
