package com.pickleball.identity.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickleball.identity.dto.*;
import com.pickleball.identity.dto.api.AuthResponse;
import com.pickleball.identity.model.User;
import com.pickleball.identity.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ApiAuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for unit testing controller logic
public class ApiAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    public void testRegister_Success() throws Exception {
//        RegisterRequest request = new RegisterRequest();
//        request.setUsername("testuser");
//        request.setPassword("password123");
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("testuser");

//        when(authService.register(any(RegisterRequest.class))).thenReturn(user);
//
//        mockMvc.perform(post("/api/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("testuser"));
//    }
//
//    @Test
//    public void testLogin_Success() throws Exception {
//        LoginRequest request = new LoginRequest();
//        request.setUsername("testuser");
//        request.setPassword("password123");
//
//        LoginResponse response = new LoginResponse(
//                "accessToken", "refreshToken", 3600L, "testuser", Collections.singletonList("USER")
//        );
//
//        when(authService.login(any(LoginRequest.class))).thenReturn(response);
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accessToken").value("accessToken"))
//                .andExpect(jsonPath("$.username").value("testuser"));
//    }
//
//    @Test
//    public void testRefresh_Success() throws Exception {
//        Map<String, String> request = Map.of("refreshToken", "validRefreshToken");
//        // RefreshResponse has accessToken, expiresIn, tokenType
//        RefreshResponse response = new RefreshResponse("newAccessToken", 3600L, "Bearer");
//
//        when(authService.refreshAccessToken("validRefreshToken")).thenReturn(response);
//
//        mockMvc.perform(post("/api/auth/refresh")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accessToken").value("newAccessToken"));
//    }
}
