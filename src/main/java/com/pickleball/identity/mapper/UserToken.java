package com.pickleball.identity.mapper;

import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public record UserToken(
        String username,
        Collection<? extends GrantedAuthority> authorities
) {}
