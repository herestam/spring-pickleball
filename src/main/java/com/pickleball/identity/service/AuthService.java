package com.pickleball.identity.service;

import com.pickleball.identity.dto.*;
import com.pickleball.identity.model.*;
import com.pickleball.identity.model.dto.LoginRequest;
import com.pickleball.identity.model.dto.RegisterRequest;
import com.pickleball.identity.repository.*;
import com.pickleball.identity.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final UserJwtRepository userJwtRepository;

    @Transactional
    public void register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("ROLE USER not found"));

        userRoleRepository.save(new UserRole(user, userRole));
    }

    public String login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        return jwtService.generateToken(request.getUsername());
    }

    @Transactional
    public void logout(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String jwt = authHeader.substring(7);
        String tokenId = jwtService.extractTokenId(jwt);

        userJwtRepository.findByTokenId(tokenId)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    userJwtRepository.save(token);
                });
    }
//
//        userJwtRepository.findByTokenId(tokenId)
//                .ifPresent(storedToken -> {
//                    storedToken.setRevoked(true);
//                    userJwtRepository.save(storedToken);
//                });
//    }
}
