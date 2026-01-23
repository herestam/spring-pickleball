package com.pickleball.identity.repository;

import com.pickleball.identity.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // Find a role by its name
    Optional<Role> findByName(String name);

    boolean existsByName(String user);
}
