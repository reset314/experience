package com.example.experience.application.user.service;

import com.example.experience.application.user.dto.*;

import java.util.List;

public interface UserService {
    UserResponse createUser(String username, String email, String rawPassword, String roleName);

    UserProfileResponse getUserProfile(String userId);

    void updatePassword(ChangePasswordRequest request);

    UserProfileResponse updateUserProfile(ChangeProfileRequest request);

    void deleteSession(String userId,String sessionId);

    void deleteAllSessions(String userId);

    List<SessionResponse> getUserSessions(String userId);

    List<UserPermissionsResponse> getUserPermissions(String userId);
}
