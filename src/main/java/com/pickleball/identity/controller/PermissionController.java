package com.pickleball.identity.controller;

import com.pickleball.identity.dto.PermissionRequest;
import com.pickleball.identity.dto.PermissionResponse;
import com.pickleball.identity.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
        return ResponseEntity.ok(
                permissionService.getAllPermissions().stream()
                        .map(PermissionResponse::fromEntity)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<PermissionResponse> createPermission(@Valid @RequestBody PermissionRequest request) {
        return ResponseEntity.ok(
                PermissionResponse.fromEntity(permissionService.createPermission(request))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponse> updatePermission(@PathVariable Long id, @Valid @RequestBody PermissionRequest request) {
        return ResponseEntity.ok(
                PermissionResponse.fromEntity(permissionService.updatePermission(id, request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}
