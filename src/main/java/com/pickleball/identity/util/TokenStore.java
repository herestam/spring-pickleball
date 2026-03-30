package com.pickleball.identity.util;

import com.pickleball.identity.mapper.UserToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TokenStore {

    // token -> user info
    private static final Map<String, UserToken> TOKENS = new ConcurrentHashMap<>();

    public static String createToken(String username, List<String> roles) {
        String token = UUID.randomUUID().toString();

        List<SimpleGrantedAuthority> authorities =
                roles.stream()
                        .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        System.out.println("authorities ===" + authorities);

        TOKENS.put(token, new UserToken(username, authorities));
        return token;
    }

    public static UserToken getUser(String token) {
        return TOKENS.get(token);
    }

    public static void revoke(String token) {
        TOKENS.remove(token);
    }
}
