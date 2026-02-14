package com.pickleball.identity.controller.api;

import com.pickleball.identity.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TokenValidationController.class)
@AutoConfigureMockMvc(addFilters = false)
@org.springframework.test.context.TestPropertySource(properties = "internal.api.key=9f3c7e2a-6b11-4d4c-a2a0-8c51d91e7b52")
public class TokenValidationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    // We need to set the value for @Value("${internal.api.key}")
    // In @WebMvcTest, we can use properties attribute or @TestPropertySource
    // But since it's a private field injected by @Value, let's use properties in @WebMvcTest annotation

    @Test
    public void testValidateToken_Success() throws Exception {
        String token = "validToken";
        Claims claims = new DefaultClaims(Map.of(
                "sub", "testuser",
                "roles", "USER",
                "exp", new Date(System.currentTimeMillis() + 10000)
        ));

        when(jwtService.parseToken(token)).thenReturn(claims);

        mockMvc.perform(post("/internal/auth/validate")
                        .header("X-API-KEY", "9f3c7e2a-6b11-4d4c-a2a0-8c51d91e7b52") // Default value from application-local.yml
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    public void testValidateToken_InvalidKey() throws Exception {
        mockMvc.perform(post("/internal/auth/validate")
                        .header("X-API-KEY", "wrong-key")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isForbidden());
    }
}
