package com.pickleball.identity.dto.api;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

    // getters & setters
    private String username;
    private String password;

}
