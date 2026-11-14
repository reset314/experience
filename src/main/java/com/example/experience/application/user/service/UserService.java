package com.example.experience.application.user.service;

import com.example.experience.application.user.dto.*;
import com.example.experience.domain.rbac.Role;

import java.util.List;

public interface UserService {
    UserResponse createUser(String username, String rawPassword, Role role);

    UserProfileResponse getUserProfile(String userId);

    void updatePassword(ChangePasswordRequest request);

    UserProfileResponse updateUserProfile(ChangeProfileRequest request);

    void deleteSession(String userId,String sessionId);

    void deleteAllSessions(String userId);

    List<SessionResponse> getUserSessions(String userId);

    List<UserPermissionsResponse> getUserPermissions(String userId);
}
