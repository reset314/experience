package com.example.experience.infrastructure.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "experience.auth")
public record AuthProperties(
    String jwtSecret,
    Duration accessTokenExpiration,
    Duration refreshTokenExpiration
) {

    @ConstructorBinding
    public AuthProperties {
        if (jwtSecret == null || jwtSecret.length() < 32) {
            throw new IllegalArgumentException("experience.auth.jwt-secret must be at least 32 characters");
        }
        if (accessTokenExpiration == null) {
            accessTokenExpiration = Duration.ofMinutes(15);
        }
        if (refreshTokenExpiration == null) {
            refreshTokenExpiration = Duration.ofDays(7);
        }
    }

    public long accessTokenExpirationSeconds() {
        return accessTokenExpiration.getSeconds();
    }

    public long refreshTokenExpirationSeconds() {
        return refreshTokenExpiration.getSeconds();
    }
}
