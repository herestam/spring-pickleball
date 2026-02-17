package com.pickleball.identity.security;

import com.pickleball.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        System.out.println("DB lookup username = '" + username + "'");

        return userRepository.findByUsernameIgnoreCase(username.trim())
                .map(user -> {
                    System.out.println("FOUND USER: " + user.getUsername());
                    return new CustomUserDetails(user);
                })
                .orElseThrow(() -> {
                    System.out.println("USER NOT FOUND IN DB!");
                    return new UsernameNotFoundException("User not found");
                });
    }
}
