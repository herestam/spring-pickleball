package com.pickleball.identity.seeder;

import com.pickleball.identity.model.*;
import com.pickleball.identity.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {

        // ✅ Prevent duplicate seed
        if (roleRepository.existsByName("USER")) {
            return;
        }

        // ===== PERMISSIONS =====
        Permission userRead = permissionRepository
                .findByName("USER_READ")
                .orElseGet(() -> permissionRepository.save(new Permission("USER_READ")));

        Permission userWrite = permissionRepository
                .findByName("USER_WRITE")
                .orElseGet(() -> permissionRepository.save(new Permission("USER_WRITE")));

        Permission adminRead = permissionRepository
                .findByName("ADMIN_READ")
                .orElseGet(() -> permissionRepository.save(new Permission("ADMIN_READ")));

        Permission adminWrite = permissionRepository
                .findByName("ADMIN_WRITE")
                .orElseGet(() -> permissionRepository.save(new Permission("ADMIN_WRITE")));

        // ===== ROLES =====
        Role userRole = new Role();
        userRole.setName("USER");
        roleRepository.save(userRole);

        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.save(adminRole);

        // ===== ROLE ↔ PERMISSION =====
        rolePermissionRepository.save(new RolePermission(userRole, userRead));
        rolePermissionRepository.save(new RolePermission(userRole, userWrite));

        rolePermissionRepository.save(new RolePermission(adminRole, userRead));
        rolePermissionRepository.save(new RolePermission(adminRole, userWrite));
        rolePermissionRepository.save(new RolePermission(adminRole, adminRead));
        rolePermissionRepository.save(new RolePermission(adminRole, adminWrite));

        // ===== ADMIN USER =====
        if (!userRepository.existsByUsername("admin")) {

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // 🔐 encoded
            admin.setEnabled(true);
            userRepository.save(admin);

            userRoleRepository.save(new UserRole(admin, adminRole));
        }
    }
}
