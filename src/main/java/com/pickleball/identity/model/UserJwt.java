package com.pickleball.identity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_jwt")
public class UserJwt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "token_id", unique = true, nullable = false, length = 255)
    private String tokenId;

    @Column(name = "expires_at", nullable = false)
    private Long expiresAt;

    private boolean revoked = false;

    // getters & setters
}
