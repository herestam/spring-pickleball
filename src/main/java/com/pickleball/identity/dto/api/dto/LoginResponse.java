package com.pickleball.identity.dto.api.dto;

import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class LoginResponse {

    private String status;
    private String username;
    private Collection<? extends GrantedAuthority> roles;

    public LoginResponse(String status, String username,
                         Collection<? extends GrantedAuthority> roles) {
        this.status = status;
        this.username = username;
        this.roles = roles;
    }

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }
}
