package com.pickleball.identity.dto;

import com.pickleball.identity.model.Role;
import lombok.Data;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class RoleResponse {
    private Long id;
    private String name;
    private Set<PermissionResponse> permissions;

    public static RoleResponse fromEntity(Role role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        if (role.getPermissions() != null) {
            response.setPermissions(role.getPermissions().stream()
                    .map(rp -> PermissionResponse.fromEntity(rp.getPermission()))
                    .collect(Collectors.toSet()));
        }
        return response;
    }
}
