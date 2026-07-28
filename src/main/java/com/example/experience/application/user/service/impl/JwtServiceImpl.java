package com.example.experience.application.user.service.impl;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.example.experience.application.user.dto.JwtClaims;
import com.example.experience.application.user.service.JwtService;
import com.example.experience.domain.user.entity.User;
import com.example.experience.infrastructure.config.AuthProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

    private final AuthProperties authProperties;
    private final SecretKey secretKey;

    public JwtServiceImpl(AuthProperties authProperties) {
        this.authProperties = authProperties;
        this.secretKey = Keys.hmacShaKeyFor(authProperties.jwtSecret().getBytes());
    }

    @Override
    public String generateAccessToken(User user, List<String> roles) {
        Instant now = Instant.now();
        Instant expiration = now.plus(authProperties.accessTokenExpiration());

        return Jwts.builder()
            .subject(user.getId())
            .claim("username", user.getUsername())
            .claim("roles", roles)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .signWith(secretKey)
            .compact();
    }

    @Override
    public JwtClaims validateAccessToken(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            String userId = claims.getSubject();
            String username = claims.get("username", String.class);
            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("roles", List.class);

            return new JwtClaims(userId, username, roles);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public long getAccessTokenExpirationSeconds() {
        return authProperties.accessTokenExpirationSeconds();
    }
}
