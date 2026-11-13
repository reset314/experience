package com.example.experience.interfaces.rest.user;

import com.example.experience.application.user.dto.*;
import com.example.experience.application.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

import java.util.List;

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

    @GetMapping("/sessions")
    public ResponseEntity<List<SessionResponse>> getSession(@AuthenticationPrincipal String userId) {
        List<SessionResponse> sessionResponse = userService.getUserSessions(userId);
        return ResponseEntity.ok(sessionResponse);
    }

    @DeleteMapping("/sessions/{id}")
    public void deleteSession(@AuthenticationPrincipal String userId, @PathVariable String id){
        userService.deleteSession(userId,id);
    }

    @DeleteMapping("/sessions")
    public void deleteSessions(@AuthenticationPrincipal String userId) {
        userService.deleteAllSessions(userId);
    }

    @GetMapping("/permissions")
    public ResponseEntity<List<UserPermissionsResponse>> getPermissions(@AuthenticationPrincipal String userId) {
        List<UserPermissionsResponse> userPermissionsResponse = userService.getUserPermissions(userId);
        return ResponseEntity.ok(userPermissionsResponse);
    }
    
}
