package com.pickleball.identity.repository;

import com.pickleball.identity.model.UserRole;
import com.pickleball.identity.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository
        extends JpaRepository<UserRole, UserRoleId> {

    List<UserRole> findByUserId(Long userId);

    boolean existsByUserIdAndRoleId(Long userId, Long roleId);
}
