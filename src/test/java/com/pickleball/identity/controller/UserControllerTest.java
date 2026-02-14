package com.pickleball.identity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickleball.identity.dto.AssignRoleRequest;
import com.pickleball.identity.dto.UpdateProfileRequest;
import com.pickleball.identity.model.User;
import com.pickleball.identity.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters to simplify testing controller logic
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;
//
//    @Test
//    @WithMockUser
//    public void testMe_Success() throws Exception {
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("testuser");
//        user.setRoles(Collections.emptySet());
//
//        when(userService.getCurrentUser()).thenReturn(user);
//
//        mockMvc.perform(get("/api/users/me"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("testuser"));
//    }
//
//    @Test
//    @WithMockUser
//    public void testUpdateProfile_Success() throws Exception {
//        UpdateProfileRequest request = new UpdateProfileRequest();
//        request.setFirstName("John");
//        request.setLastName("Doe");
//        request.setEmail("john@example.com");
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("testuser");
//        user.setRoles(Collections.emptySet()); // Add empty roles to avoid NullPointerException in UserResponse.fromEntity
//
//        User updatedUser = new User();
//        updatedUser.setId(1L);
//        updatedUser.setUsername("testuser");
//        updatedUser.setFirstName("John");
//        updatedUser.setLastName("Doe");
//        updatedUser.setEmail("john@example.com");
//        updatedUser.setRoles(Collections.emptySet());
//
//        when(userService.getCurrentUser()).thenReturn(user);
//        when(userService.updateUserProfile(eq(1L), any(UpdateProfileRequest.class))).thenReturn(updatedUser);
//
//        mockMvc.perform(put("/api/users/me")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.firstName").value("John"));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void testAssignRole_Success() throws Exception {
//        AssignRoleRequest request = new AssignRoleRequest();
//        request.setRoleName("ADMIN");
//
//        doNothing().when(userService).assignRoleToUser(eq(1L), eq("ADMIN"));
//
//        mockMvc.perform(post("/api/users/1/roles")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk());
//    }
}
