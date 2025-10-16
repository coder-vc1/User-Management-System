package com.example.usermanagement.controller;

import com.example.usermanagement.dto.UserResponseDto;
import com.example.usermanagement.exception.UserNotFoundException;
import com.example.usermanagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllUsers_ShouldReturnUsers() throws Exception {
        UserResponseDto user1 = new UserResponseDto(1L, "John", "Doe", "123-45-6789", "john@example.com", 30, "admin", "123-456-7890", "johnd", "1993-01-01", "male");
        UserResponseDto user2 = new UserResponseDto(2L, "Jane", "Smith", "987-65-4321", "jane@example.com", 25, "user", "098-765-4321", "janes", "1998-05-15", "female");
        List<UserResponseDto> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
        UserResponseDto user = new UserResponseDto(1L, "John", "Doe", "123-45-6789", "john@example.com", 30, "admin", "123-456-7890", "johnd", "1993-01-01", "male");

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void getUserById_WhenUserNotFound_ShouldReturn404() throws Exception {
        when(userService.getUserById(anyLong())).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserByEmail_WhenUserExists_ShouldReturnUser() throws Exception {
        UserResponseDto user = new UserResponseDto(1L, "John", "Doe", "123-45-6789", "john@example.com", 30, "admin", "123-456-7890", "johnd", "1993-01-01", "male");

        when(userService.getUserByEmail("john@example.com")).thenReturn(user);

        mockMvc.perform(get("/api/users/email/john@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void searchUsers_ShouldReturnMatchingUsers() throws Exception {
        UserResponseDto user = new UserResponseDto(1L, "John", "Doe", "123-45-6789", "john@example.com", 30, "admin", "123-456-7890", "johnd", "1993-01-01", "male");
        List<UserResponseDto> users = List.of(user);

        when(userService.searchUsers("John")).thenReturn(users);

        mockMvc.perform(get("/api/users/search").param("q", "John"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void searchUsers_WithoutSearchTerm_ShouldReturnAllUsers() throws Exception {
        UserResponseDto user1 = new UserResponseDto(1L, "John", "Doe", "123-45-6789", "john@example.com", 30, "admin", "123-456-7890", "johnd", "1993-01-01", "male");
        UserResponseDto user2 = new UserResponseDto(2L, "Jane", "Smith", "987-65-4321", "jane@example.com", 25, "user", "098-765-4321", "janes", "1998-05-15", "female");
        List<UserResponseDto> users = Arrays.asList(user1, user2);

        when(userService.searchUsers(any())).thenReturn(users);

        mockMvc.perform(get("/api/users/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}