package com.example.experience.application.user.service;

import com.example.experience.application.user.dto.DeviceInfo;
import com.example.experience.application.user.dto.RefreshRequest;
import com.example.experience.application.user.dto.TokenResponse;

public interface AuthenticationService {

    TokenResponse login(String username, String rawPassword, DeviceInfo deviceInfo);

    TokenResponse refresh(RefreshRequest request, DeviceInfo deviceInfo);

    void logout(String refreshToken);

    void logoutAll(String userId);
}
