package com.pickleball.identity.repository;

import com.pickleball.identity.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {"roles", "roles.role"})
    Optional<User> findByUsernameIgnoreCase(String username);
    Boolean existsByUsername(String username);

    @Query("""
        SELECT u FROM User u
        JOIN u.roles ur
        JOIN ur.role r
        WHERE r.name = :roleName
    """)
    List<User> findUsersByRole(String roleName);
}
