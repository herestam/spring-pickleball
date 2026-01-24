package com.pickleball.identity.controller.api;

import com.pickleball.identity.dto.api.AuthResponse;
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
        authService.register(request);
        return ResponseEntity.ok("Register success");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok("Logged out");
    }

    @PostMapping("/login-auth")
    public ResponseEntity<LoginResponse> loginAuth(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginAuth(request));
    }

    // 🔄 REFRESH TOKEN
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshToken(@RequestBody Map<String, String> body) {

        String refreshToken = body.get("refreshToken");

        System.out.println("refresh token " + refreshToken);

        RefreshResponse response = authService.refreshAccessToken(refreshToken);

        return ResponseEntity.ok(response);
    }
}
