package com.pickleball.identity.dto.api.dto;

import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserResponse {

    private Long id;
    private String username;
    private Set<String> roles;

    public static UserResponse fromEntity(com.pickleball.identity.model.User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRoles(
                user.getRoles().stream()
                        .map(ur -> ur.getRole().getName())
                        .collect(Collectors.toSet())
        );
        return dto;
    }
}
