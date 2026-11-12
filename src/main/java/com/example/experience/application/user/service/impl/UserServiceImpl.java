package com.example.experience.application.user.service.impl;

import com.example.experience.application.user.dto.ChangePasswordRequest;
import com.example.experience.application.user.dto.ChangeProfileRequest;
import com.example.experience.application.user.dto.UserProfileResponse;
import com.example.experience.application.user.dto.UserResponse;
import com.example.experience.application.user.service.UserService;
import com.example.experience.common.exception.AccessDeniedException;
import com.example.experience.common.utils.Uuid7Utils;
import com.example.experience.domain.rbac.entity.UserRole;
import com.example.experience.domain.rbac.repository.RoleRepository;
import com.example.experience.domain.rbac.repository.UserRoleRepository;
import com.example.experience.domain.user.entity.User;
import com.example.experience.domain.user.entity.UserProfile;
import com.example.experience.domain.user.repository.UserProfileRepository;
import com.example.experience.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse createUser(String username, String email, String rawPassword, String roleName) {
        String userId = Uuid7Utils.generateUuid7();
        User user = User.create(userId, username, email, passwordEncoder.encode(rawPassword));
        User saved = userRepository.save(user);

        String profileId = Uuid7Utils.generateUuid7();
        UserProfile profile = UserProfile.builder()
            .id(profileId)
            .user(saved)
            .displayName(username)
            .build();
        userProfileRepository.save(profile);

        roleRepository.findByName(roleName).ifPresent(role -> {
            UserRole userRole = UserRole.create(Uuid7Utils.generateUuid7(), saved, role);
            userRoleRepository.save(userRole);
        });

        return new UserResponse(saved.getId(), saved.getUsername(), roleName);
    }

    @Override 
    public UserProfileResponse getUserProfile(String userId) {

        UserProfile profile = userProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("User profile not found"));
        
        return new UserProfileResponse(
            userId,
            profile.getDisplayName(),
            profile.getAvatarData()
        );
    }

    @Override
    public void updatePassword(ChangePasswordRequest request) {
        User user = userRepository.getReferenceById(request.userId());

        if (!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
            throw new AccessDeniedException("Old password does not match user's password");
        } else {
            user.changePassword(passwordEncoder.encode(request.oldPassword()));
        }

    }

    @Override
    public UserProfileResponse updateUserProfile(ChangeProfileRequest request){
        UserProfile userProfile = userProfileRepository.findByUserId(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("User profile not found"));
        userProfile.setDisplayName(request.displayName());
        userProfile.setSettings(request.settings().toString());
        userProfile.setAvatarData(request.avatarUri());
        userProfileRepository.save(userProfile);
        return new UserProfileResponse(userProfile.getId(), userProfile.getDisplayName(), userProfile.getAvatarData());
    }
}
