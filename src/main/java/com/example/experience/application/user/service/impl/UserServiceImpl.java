package com.example.experience.application.user.service.impl;

import com.example.experience.application.user.dto.*;
import com.example.experience.application.user.service.UserService;
import com.example.experience.common.exception.AccessDeniedException;
import com.example.experience.common.utils.Uuid7Utils;
import com.example.experience.domain.rbac.Role;
import com.example.experience.domain.rbac.entity.UserRole;
import com.example.experience.domain.rbac.repository.UserRoleRepository;
import com.example.experience.domain.user.entity.RevokeReason;
import com.example.experience.domain.user.entity.User;
import com.example.experience.domain.user.entity.UserProfile;
import com.example.experience.domain.user.entity.UserSession;
import com.example.experience.domain.user.repository.UserProfileRepository;
import com.example.experience.domain.user.repository.UserRepository;
import com.example.experience.domain.user.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserSessionRepository userSessionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse createUser(String username, String rawPassword, Role role) {
        String userId = Uuid7Utils.generateUuid7();
        User user = User.create(userId, username, passwordEncoder.encode(rawPassword));
        User saved = userRepository.save(user);

        String profileId = Uuid7Utils.generateUuid7();
        UserProfile profile = UserProfile.builder()
            .id(profileId)
            .user(saved)
            .displayName(username)
            .build();
        userProfileRepository.save(profile);

        userRoleRepository.save(UserRole.create(saved, role));

        return new UserResponse(saved.getId(), saved.getUsername(), List.of(role.getCode()));
    }

    @Override 
    public UserProfileResponse getUserProfile(String userId) {

        UserProfile profile = userProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("User profile not found"));
        
        return new UserProfileResponse(
            userId,
            profile.getDisplayName(),
            profile.getAvatarUri()
        );
    }

    @Transactional
    @Override
    public void updatePassword(ChangePasswordRequest request) {
        User user = userRepository.getReferenceById(request.userId());

        if (!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
            throw new AccessDeniedException("Old password does not match user's password");
        } else {
            user.changePassword(passwordEncoder.encode(request.oldPassword()));
        }

    }

    @Transactional
    @Override
    public UserProfileResponse updateUserProfile(ChangeProfileRequest request){
        UserProfile userProfile = userProfileRepository.findByUserId(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("User profile not found"));
        userProfile.setDisplayName(request.displayName());
        userProfile.setSettings(request.settings().toString());
        userProfile.setAvatarUri(request.avatarUri());
        userProfileRepository.save(userProfile);
        return new UserProfileResponse(userProfile.getId(), userProfile.getDisplayName(), userProfile.getAvatarUri());
    }

    @Transactional
    @Override
    public void deleteSession(String userId,String sessionId) {
        UserSession userSession = userSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("User session not found"));
        if (!userSession.getUser().getId().equals(userId))
            throw new AccessDeniedException("No access to session");
        else if (userSession.isActive())
            userSession.revoke(RevokeReason.LOGOUT);
    }

    @Transactional
    @Override
    public void deleteAllSessions(String userId) {
        List<UserSession> userSessions = userSessionRepository.findByUserId(userId);
        for (UserSession userSession : userSessions) {
            if (userSession.isActive())
                userSession.revoke(RevokeReason.LOGOUT_ALL);
        }
    }

    @Override
    public List<SessionResponse>  getUserSessions(String userId) {
        List<UserSession> userSessions = userSessionRepository.findByUserId(userId);
        List<SessionResponse> sessionResponses = new ArrayList<>();
        for (UserSession userSession : userSessions)
            if(userSession.isActive())
                sessionResponses.add(new SessionResponse(
                    userSession.getId(),
                    userSession.getStatus(),
                    userSession.getDeviceName(),
                    userSession.getIpAddress(),
                    userSession.getUserAgent(),
                    userSession.getIssuedAt(),
                    userSession.getUpdatedAt(),
                    userSession.getExpiresAt()
                    ));
        return sessionResponses;
    }

    @Override
    public List<UserPermissionsResponse> getUserPermissions(String userId) {
        //TODO::如函数名
        return null;
    }
}
