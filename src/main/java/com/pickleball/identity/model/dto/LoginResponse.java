package com.pickleball.identity.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private long expiresAt;
    private List<String> roles;

    public LoginResponse(String accessToken, long expiresAt, List<String> roles) {
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
        this.roles = roles;
    }
}