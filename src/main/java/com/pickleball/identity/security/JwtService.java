package com.pickleball.identity.security;

import com.pickleball.identity.model.User;
import com.pickleball.identity.model.UserJwt;
import com.pickleball.identity.dto.RefreshResponse;
import com.pickleball.identity.repository.UserJwtRepository;
import com.pickleball.identity.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private final SecretKey key;
    private final long expiration;

    private final UserJwtRepository userJwtRepository;
    private final UserRepository userRepository;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

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

    public String generateAccessToken(User user) {

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getRoles().stream()
                        .map(userRole -> userRole.getRole().getName())
                        .toList())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshToken(User user) {

        String tokenId = UUID.randomUUID().toString();
        Date expiryDate = new Date(System.currentTimeMillis() + refreshExpiration);

        String refreshToken = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("tokenId", tokenId)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getKey())
                .compact();

        UserJwt userJwt = new UserJwt();
        userJwt.setUserId(user.getId());
        userJwt.setTokenId(tokenId);
        userJwt.setExpiresAt(expiryDate.getTime());
        userJwtRepository.save(userJwt);

        return refreshToken;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public RefreshResponse refreshAccessToken(String refreshToken) {

        Claims claims = parseClaims(refreshToken);

        String tokenId = claims.get("tokenId", String.class);
        UserJwt storedToken = userJwtRepository.findByTokenId(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        if (storedToken.isRevoked()) {
            throw new RuntimeException("Token revoked");
        }

        if (storedToken.getExpiresAt() < System.currentTimeMillis()) {
            throw new RuntimeException("Refresh token expired");
        }

        String username = claims.getSubject();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow();

        return new RefreshResponse(
                generateAccessToken(user),
                accessExpiration / 1000,   // seconds
                "Bearer"
        );

    }
}
