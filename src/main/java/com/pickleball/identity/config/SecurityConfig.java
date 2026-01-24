package com.pickleball.identity.config;

import com.pickleball.identity.security.CustomUserDetailsService;
import com.pickleball.identity.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .sessionManagement(s ->
//                        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//
//                .authenticationProvider(authenticationProvider())
//
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/",
//                                "/actuator/health/**",
//                                "/api/auth/**",
//
//                                // 🔓 SWAGGER / OPENAPI
//                                "/v3/api-docs/**",
//                                "/swagger-ui.html",
//                                "/swagger-ui/**",
//
//                                // 🔓 H2 CONSOLE
//                                "/h2-console/**"
//                        ).permitAll()
////                        .requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/api/**").hasAnyRole("USER", "ADMIN")
//                        .anyRequest().authenticated()
//                )
//
//                // 🔥 CUSTOM TOKEN FILTER
//                .addFilterBefore(
//                        new TokenAuthenticationFilter(),
//                        UsernamePasswordAuthenticationFilter.class
//                )
//
//                .csrf(csrf -> csrf.disable())
//                .formLogin(form -> form.disable())
//                .logout(logout -> logout.disable());
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> {})
                // 🧪 CSRF (disable only where needed)
                .csrf(csrf -> csrf.disable())
                // 🔐 AUTHORIZATION
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/actuator/health/**",
                                "/api/auth/**",

                                // 🔓 SWAGGER
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",

                                // 🔓 H2
                                "/h2-console/**"
                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )

                // 🖼️ H2 uses frames
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                ).addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);;

        return http.build();
    }


}
