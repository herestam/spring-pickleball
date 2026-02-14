package com.pickleball.identity.service;

import com.pickleball.identity.dto.RoleRequest;
import com.pickleball.identity.model.Permission;
import com.pickleball.identity.model.Role;
import com.pickleball.identity.model.RolePermission;
import com.pickleball.identity.repository.PermissionRepository;
import com.pickleball.identity.repository.RolePermissionRepository;
import com.pickleball.identity.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    public Role createRole(RoleRequest request) {
        if (roleRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Role already exists: " + request.getName());
        }

        Role role = new Role();
        role.setName(request.getName());
        role = roleRepository.save(role);

        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            updateRolePermissions(role, request.getPermissionIds());
        }

        return role;
    }

    @Transactional
    public Role updateRole(Long id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setName(request.getName());
        
        if (request.getPermissionIds() != null) {
            // clear existing permissions
             // efficient way depends on cascade settings, but let's do it explicitly via repository or entity
            // Since Role has orphanRemoval=true, clearing the set should work if we fetch it nicely.
            // But modifying the collection directly is safer with Hibernate.
            
            // However, we have a RolePermission repository.
            // Let's rely on the Set in Role to manage this via orphanRemoval
            role.getPermissions().clear();
            updateRolePermissions(role, request.getPermissionIds());
        }
        
        return roleRepository.save(role);
    }
    
    private void updateRolePermissions(Role role, Set<Long> permissionIds) {
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        for (Permission permission : permissions) {
            RolePermission rolePermission = new RolePermission(role, permission);
            role.getPermissions().add(rolePermission);
        }
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
