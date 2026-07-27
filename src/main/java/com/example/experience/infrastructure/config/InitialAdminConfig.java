package com.example.experience.infrastructure.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import com.example.experience.application.user.service.UserService;
import com.example.experience.domain.user.repository.UserRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "experience.initial-admin")
@Getter
@Setter
public class InitialAdminConfig {

    private final UserRepository userRepository;
    private final UserService userService;

    private String username;
    private String password;

    @EventListener(ApplicationReadyEvent.class)
    public void createInitialAdmin() {
        if (username == null || password == null) {
            log.warn("Initial admin credentials not configured");
            return;
        }
        if (userRepository.findByUsername(username).isEmpty()) {
            userService.createUser(username, password, "superadmin");
            log.info("Created initial superadmin user: {}", username);
        }
    }
}
