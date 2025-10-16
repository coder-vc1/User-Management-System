package com.example.usermanagement.service;

import com.example.usermanagement.dto.DummyJsonResponseDto;
import com.example.usermanagement.dto.DummyJsonUserDto;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.DataLoadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataLoadServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserService userService;

    @InjectMocks
    private DataLoadService dataLoadService;

    private DummyJsonResponseDto mockResponse;
    private DummyJsonUserDto mockUserDto;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(dataLoadService, "baseUrl", "https://dummyjson.com");
        
        mockUserDto = new DummyJsonUserDto();
        mockUserDto.setId(1L);
        mockUserDto.setFirstName("John");
        mockUserDto.setLastName("Doe");
        mockUserDto.setSsn("123-45-6789");
        mockUserDto.setEmail("john@example.com");
        mockUserDto.setAge(30);
        mockUserDto.setRole("admin");

        mockResponse = new DummyJsonResponseDto();
        mockResponse.setUsers(List.of(mockUserDto));
        mockResponse.setTotal(1);
        mockResponse.setSkip(0);
        mockResponse.setLimit(30);
    }

    @Test
    void loadUsersFromExternalAPI_WhenUsersAlreadyExist_ShouldSkipLoading() {
        when(userService.getUserCount()).thenReturn(5L);

        dataLoadService.loadUsersFromExternalAPI();

        verify(userService).getUserCount();
        verify(restTemplate, never()).getForObject(anyString(), eq(DummyJsonResponseDto.class));
        verify(userService, never()).saveAllUsers(anyList());
    }

    @Test
    void loadUsersFromExternalAPI_WhenNoUsersExist_ShouldLoadFromAPI() {
        when(userService.getUserCount()).thenReturn(0L);
        when(restTemplate.getForObject(anyString(), eq(DummyJsonResponseDto.class)))
                .thenReturn(mockResponse);

        dataLoadService.loadUsersFromExternalAPI();

        verify(userService).getUserCount();
        verify(restTemplate).getForObject(anyString(), eq(DummyJsonResponseDto.class));
        verify(userService).saveAllUsers(anyList());
        verify(userService).indexAllUsers();
    }

    @Test
    void loadUsersFromExternalAPI_WhenAPIReturnsNull_ShouldThrowException() {
        when(userService.getUserCount()).thenReturn(0L);
        when(restTemplate.getForObject(anyString(), eq(DummyJsonResponseDto.class)))
                .thenReturn(null);

        assertThrows(DataLoadException.class, () -> dataLoadService.loadUsersFromExternalAPI());
        
        verify(userService, never()).saveAllUsers(anyList());
        verify(userService, never()).indexAllUsers();
    }

    @Test
    void loadUsersFromExternalAPI_WhenAPIThrowsException_ShouldThrowDataLoadException() {
        when(userService.getUserCount()).thenReturn(0L);
        when(restTemplate.getForObject(anyString(), eq(DummyJsonResponseDto.class)))
                .thenThrow(new RuntimeException("API Error"));

        assertThrows(DataLoadException.class, () -> dataLoadService.loadUsersFromExternalAPI());
        
        verify(userService, never()).saveAllUsers(anyList());
        verify(userService, never()).indexAllUsers();
    }

    @Test
    void loadUsersFromExternalAPI_WithMultiplePages_ShouldLoadAllUsers() {
        when(userService.getUserCount()).thenReturn(0L);
        
        DummyJsonUserDto user2 = new DummyJsonUserDto();
        user2.setId(2L);
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setSsn("987-65-4321");
        user2.setEmail("jane@example.com");
        user2.setAge(25);
        user2.setRole("user");

        DummyJsonResponseDto firstResponse = new DummyJsonResponseDto();
        firstResponse.setUsers(List.of(mockUserDto));
        firstResponse.setTotal(2);
        firstResponse.setSkip(0);
        firstResponse.setLimit(30);

        DummyJsonResponseDto secondResponse = new DummyJsonResponseDto();
        secondResponse.setUsers(List.of(user2));
        secondResponse.setTotal(2);
        secondResponse.setSkip(30);
        secondResponse.setLimit(30);

        when(restTemplate.getForObject(contains("skip=0"), eq(DummyJsonResponseDto.class)))
                .thenReturn(firstResponse);
        when(restTemplate.getForObject(contains("skip=30"), eq(DummyJsonResponseDto.class)))
                .thenReturn(secondResponse);

        dataLoadService.loadUsersFromExternalAPI();

        verify(restTemplate, times(2)).getForObject(anyString(), eq(DummyJsonResponseDto.class));
        verify(userService).saveAllUsers(argThat(users -> users.size() == 2));
        verify(userService).indexAllUsers();
    }
}