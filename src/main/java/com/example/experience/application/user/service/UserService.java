package com.example.experience.application.user.service;

import com.example.experience.application.user.dto.UserResponse;

public interface UserService {
    UserResponse createUser(String username, String email, String rawPassword, String roleName);
}
