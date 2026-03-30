package com.pickleball.identity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickleball.identity.dto.RoleRequest;
import com.pickleball.identity.dto.RoleResponse;
import com.pickleball.identity.model.Role;
import com.pickleball.identity.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;


    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        try{
            List<Role> roles = roleService.getAllRoles();

            List<Role> roll = roles.stream().filter(r -> "USER".equals(r.getName())).toList();

            for (Role r : roll){
                System.out.println("role user" + r.getName());
            }

            roles.forEach(r -> {
                        System.out.println("ROles ===>" + r.getName());
                        r.getPermissions().forEach(
                                rp -> System.out.println(
                                        "permission ==> debug" + rp.getPermission().getName()
                                )
                        );

                    }
            );
        } catch (Exception e) {
            System.out.println("failllllllll");
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(
                roleService.getAllRoles().stream()
                        .map(RoleResponse::fromEntity)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest request) {
        return ResponseEntity.ok(
                RoleResponse.fromEntity(roleService.createRole(request))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable Long id, @Valid @RequestBody RoleRequest request) {
        return ResponseEntity.ok(
                RoleResponse.fromEntity(roleService.updateRole(id, request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
