package com.example.experience.interfaces.rest.user;

import com.example.experience.application.user.dto.ChangePasswordRequest;
import com.example.experience.application.user.dto.ChangeProfileRequest;
import com.example.experience.application.user.dto.UserProfileResponse;
import com.example.experience.application.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class UserInfoController {
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<UserProfileResponse> getUserInfo(@AuthenticationPrincipal String userId) {
        UserProfileResponse userProfileResponse = userService.getUserProfile(userId);
        return ResponseEntity.ok(userProfileResponse);
    }

    @PatchMapping("/password")
    public void changePassword(@AuthenticationPrincipal String userId,
                                                       @RequestBody String oldpassword,
                                                                    String newPassword) {
        ChangePasswordRequest cpreq = new ChangePasswordRequest(userId, oldpassword, newPassword);
        userService.updatePassword(cpreq);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@AuthenticationPrincipal String userId) {
        UserProfileResponse userProfileResponse = userService.getUserProfile(userId);
        return ResponseEntity.ok(userProfileResponse);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> changeUserProfile(@AuthenticationPrincipal String userId,
                                                                 @RequestBody String diaplayName,
                                                                 String avatarUri,
                                                                 JsonNode setting){
        ChangeProfileRequest cprreq = new ChangeProfileRequest(userId, avatarUri, diaplayName, setting);
        UserProfileResponse userProfileResponse = userService.updateUserProfile(cprreq);
        return ResponseEntity.ok(userProfileResponse);

    }

    
}
