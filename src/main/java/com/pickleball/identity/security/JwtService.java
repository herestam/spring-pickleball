package com.pickleball.identity.security;

import com.pickleball.identity.model.User;
import com.pickleball.identity.model.UserJwt;
import com.pickleball.identity.repository.UserJwtRepository;
import com.pickleball.identity.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expiration;

    private final UserJwtRepository userJwtRepository;
    private final UserRepository userRepository;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration, UserJwtRepository userJwtRepository, UserRepository userRepository
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
        this.userJwtRepository = userJwtRepository;
        this.userRepository = userRepository;
    }

    public String generateToken(String username) {

        String tokenId = UUID.randomUUID().toString();
        Date expiryDate = new Date(System.currentTimeMillis() + expiration);
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow();

//        return Jwts.builder()
//                .setSubject(username)   // ← works in 0.11.x
//                .claim("tokenId", tokenId)
//                .setIssuedAt(new Date())
//                .setExpiration(expiryDate)
//                .signWith(key)
//                .compact();

        String token = Jwts.builder()
                .setSubject(username)
                .claim("tokenId", tokenId)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();

        // 🔥 SAVE TOKEN TO DB
        UserJwt userJwt = new UserJwt();
        userJwt.setUserId(user.getId());
        userJwt.setTokenId(tokenId);
        userJwt.setExpiresAt(expiryDate.getTime());
        userJwtRepository.save(userJwt);

        return token;
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            extractUsername(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractTokenId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("tokenId", String.class);
    }
}
