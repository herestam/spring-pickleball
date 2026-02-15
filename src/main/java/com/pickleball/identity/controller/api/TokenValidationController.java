package com.pickleball.identity.controller.api;

import com.pickleball.identity.security.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@RestController
@RequestMapping("/internal/auth")
public class TokenValidationController {

    @Value("${internal.api.key}")
    private String internalKey;

    private final JwtService jwtService;

    public TokenValidationController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(
            @RequestHeader("Authorization") String authHeader,
            @RequestHeader("X-API-KEY") String apiKey
    ) {
        if (!internalKey.equals(apiKey)) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "Forbidden"));
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("valid", false, "error", "Missing token"));
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtService.parseToken(token);

            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "username", claims.getSubject(),
                    "roles", claims.get("roles"),
                    "expiresAt", claims.getExpiration().getTime()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(Map.of("valid", false, "error", "Invalid or expired token"));
        }
    }
}
