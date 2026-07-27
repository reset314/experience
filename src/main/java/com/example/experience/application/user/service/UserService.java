package com.example.experience.application.user.service;

import com.example.experience.application.user.dto.UserResponse;

public interface UserService {
    UserResponse createUser(String username, String rawPassword, String roleName);
}
