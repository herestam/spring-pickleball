package com.pickleball.identity.dto.api;

import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserResponse {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Set<String> roles;

    public static UserResponse fromEntity(com.pickleball.identity.model.User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRoles(
                user.getRoles().stream()
                        .map(ur -> ur.getRole().getName())
                        .collect(Collectors.toSet())
        );
        return dto;
    }
}
