package com.pickleball.identity.repository;

import com.pickleball.identity.model.RolePermission;
import com.pickleball.identity.model.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository
        extends JpaRepository<RolePermission, RolePermissionId> {
}
