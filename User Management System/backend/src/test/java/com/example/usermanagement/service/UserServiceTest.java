package com.example.usermanagement.service;

import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.UserNotFoundException;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.repository.UserSearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSearchRepository userSearchRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private List<User> testUsers;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "John", "Doe", "123-45-6789", "john.doe@example.com", 30, "admin");
        User testUser2 = new User(2L, "Jane", "Smith", "987-65-4321", "jane.smith@example.com", 25, "user");
        testUsers = Arrays.asList(testUser, testUser2);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(testUsers);

        var result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        var result = userService.getUserById(1L);

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserByEmail_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));

        var result = userService.getUserByEmail("john.doe@example.com");

        assertEquals("John", result.getFirstName());
        verify(userRepository).findByEmail("john.doe@example.com");
    }

    @Test
    void getUserByEmail_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("nonexistent@example.com"));
        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void searchUsers_WhenSearchTermIsEmpty_ShouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(testUsers);

        var result = userService.searchUsers("");

        assertEquals(2, result.size());
        verify(userRepository).findAll();
        verify(userSearchRepository, never()).fullTextSearch(anyString());
    }

    @Test
    void searchUsers_WhenSearchTermIsLessThan3Characters_ShouldUseBasicSearch() {
        when(userRepository.findBySearchTerm("Jo")).thenReturn(List.of(testUser));

        var result = userService.searchUsers("Jo");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(userRepository).findBySearchTerm("Jo");
        verify(userSearchRepository, never()).fullTextSearch(anyString());
    }

    @Test
    void searchUsers_WhenSearchTermIs3OrMoreCharacters_ShouldUseFullTextSearch() {
        when(userSearchRepository.fullTextSearch("John")).thenReturn(List.of(testUser));

        var result = userService.searchUsers("John");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(userSearchRepository).fullTextSearch("John");
        verify(userRepository, never()).findBySearchTerm(anyString());
    }

    @Test
    void saveUser_ShouldCallRepository() {
        userService.saveUser(testUser);

        verify(userRepository).save(testUser);
    }

    @Test
    void saveAllUsers_ShouldCallRepository() {
        userService.saveAllUsers(testUsers);

        verify(userRepository).saveAll(testUsers);
    }

    @Test
    void getUserCount_ShouldReturnCount() {
        when(userRepository.count()).thenReturn(5L);

        long result = userService.getUserCount();

        assertEquals(5L, result);
        verify(userRepository).count();
    }

    @Test
    void indexAllUsers_ShouldCallSearchRepository() {
        userService.indexAllUsers();

        verify(userSearchRepository).indexAllUsers();
    }
}