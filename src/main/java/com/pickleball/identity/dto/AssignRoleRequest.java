package com.pickleball.identity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignRoleRequest {
    @NotBlank(message = "Role name is required")
    private String roleName;
}
