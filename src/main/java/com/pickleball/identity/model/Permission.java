package com.pickleball.identity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String name; // USER_READ, USER_WRITE

    // ✅ REQUIRED by JPA
    protected Permission() {
    }

    // ✅ USED by seed / business logic
    public Permission(String name) {
        this.name = name;
    }

    // getters & setters
}
