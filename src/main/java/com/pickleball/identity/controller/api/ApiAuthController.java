package com.pickleball.identity.controller.api;

import com.pickleball.identity.dto.LoginRequest;
import com.pickleball.identity.dto.LoginResponse;
import com.pickleball.identity.dto.RefreshResponse;
import com.pickleball.identity.dto.RegisterRequest;
import com.pickleball.identity.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok("Logged out");
    }

    @PostMapping("/login-auth")
    public ResponseEntity<LoginResponse> loginAuth(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginAuth(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody com.pickleball.identity.dto.ChangePasswordRequest request, java.security.Principal principal) {
        authService.changePassword(request, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

    // 🔄 REFRESH TOKEN
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshToken(@Valid @RequestBody com.pickleball.identity.dto.RefreshTokenRequest request) {

        System.out.println("refresh token " + request.getRefreshToken());

        RefreshResponse response = authService.refreshAccessToken(request.getRefreshToken());

        return ResponseEntity.ok(response);
    }
}
