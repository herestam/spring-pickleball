package com.pickleball.identity.repository;

import com.pickleball.identity.model.UserJwt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJwtRepository extends JpaRepository<UserJwt, Long> {

    Optional<UserJwt> findByTokenId(String tokenId);
}
