package com.pickleball.identity.dto;

import com.pickleball.identity.model.Permission;
import lombok.Data;

@Data
public class PermissionResponse {
    private Long id;
    private String name;

    public static PermissionResponse fromEntity(Permission permission) {
        PermissionResponse response = new PermissionResponse();
        response.setId(permission.getId());
        response.setName(permission.getName());
        return response;
    }
}
