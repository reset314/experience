package com.example.experience.interfaces.rest.user;

import com.example.experience.application.user.dto.*;
import com.example.experience.application.user.service.AuthenticationService;
import com.example.experience.application.user.service.UserService;
import com.example.experience.domain.rbac.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        UserResponse response = userService.createUser(
            request.username(),
            request.password(),
            Role.USER
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request,
                                               HttpServletRequest httpRequest,
                                               @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        DeviceInfo deviceInfo = new DeviceInfo(
            null,
            httpRequest.getRemoteAddr(),
            userAgent
        );
        TokenResponse response = authenticationService.login(
            request.username(),
            request.password(),
            deviceInfo
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request,
                                                 HttpServletRequest httpRequest,
                                                 @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        DeviceInfo deviceInfo = new DeviceInfo(
            null,
            httpRequest.getRemoteAddr(),
            userAgent
        );
        TokenResponse response = authenticationService.refresh(request, deviceInfo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshRequest request) {
        authenticationService.logout(request.refreshToken());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(@AuthenticationPrincipal String userId) {
        authenticationService.logoutAll(userId);
        return ResponseEntity.ok().build();
    }
}
