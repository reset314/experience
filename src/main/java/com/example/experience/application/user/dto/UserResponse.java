package com.example.experience.application.user.dto;

import java.util.List;

public record UserResponse(String id, String username, List<String> roles) {
}
