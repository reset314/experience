package com.example.experience.interfaces.rest.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.experience.application.user.dto.LoginRequest;
import com.example.experience.application.user.dto.UserResponse;
import com.example.experience.application.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody LoginRequest request) {
        UserResponse response = userService.createUser(request.username(), request.password(), "user");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        // For stateless/JWT future: placeholder returning username
        return ResponseEntity.ok("Authenticated: " + request.username());
    }
}
