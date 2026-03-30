package com.pickleball.identity.controller;

import com.pickleball.identity.dto.api.UserResponse;
import com.pickleball.identity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ ADMIN only
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    // ✅ ADMIN only
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsersByRole(@PathVariable String role) {
        return userService.getUsersByRole(role)
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    // ✅ USER / ADMIN
    @GetMapping("/me")
    public UserResponse me() {
        return UserResponse.fromEntity(
                userService.getCurrentUser()
        );
    }

    // ✅ USER update profile
    @PutMapping("/me")
    public UserResponse updateProfile(@jakarta.validation.Valid @RequestBody com.pickleball.identity.dto.UpdateProfileRequest request) {
        return UserResponse.fromEntity(
                userService.updateUserProfile(userService.getCurrentUser().getId(), request)
        );
    }

    // ✅ ADMIN assign role
    @PostMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public void assignRole(@PathVariable Long id, @jakarta.validation.Valid @RequestBody com.pickleball.identity.dto.AssignRoleRequest request) {
        userService.assignRoleToUser(id, request.getRoleName());
    }
}
