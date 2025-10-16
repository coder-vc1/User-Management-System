package com.example.usermanagement.service;

import com.example.usermanagement.dto.DummyJsonResponseDto;
import com.example.usermanagement.dto.DummyJsonUserDto;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.DataLoadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataLoadService {

    private static final Logger logger = LoggerFactory.getLogger(DataLoadService.class);

    private final RestTemplate restTemplate;
    private final UserService userService;

    @Value("${api.external.dummyjson.base-url}")
    private String baseUrl;

    public DataLoadService(RestTemplate restTemplate, UserService userService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    @Retryable(
        retryFor = {Exception.class},
        maxAttemptsExpression = "${api.external.dummyjson.retry.max-attempts}",
        backoff = @Backoff(delayExpression = "${api.external.dummyjson.retry.delay}")
    )
    public void loadUsersFromExternalAPI() {
        logger.info("Starting to load users from external API");
        
        try {
            long existingUserCount = userService.getUserCount();
            if (existingUserCount > 0) {
                logger.info("Users already exist in database ({}). Skipping data load.", existingUserCount);
                return;
            }

            List<User> allUsers = new ArrayList<>();
            int limit = 30;
            int skip = 0;
            int totalUsers = 0;

            do {
                String url = String.format("%s/users?limit=%d&skip=%d", baseUrl, limit, skip);
                logger.debug("Fetching users from: {}", url);

                DummyJsonResponseDto response = restTemplate.getForObject(url, DummyJsonResponseDto.class);
                
                if (response == null || response.getUsers() == null) {
                    throw new DataLoadException("Invalid response from external API");
                }

                totalUsers = response.getTotal();
                List<User> users = response.getUsers().stream()
                        .map(this::convertToUser)
                        .collect(Collectors.toList());

                allUsers.addAll(users);
                skip += limit;

                logger.debug("Loaded {} users, total so far: {}", users.size(), allUsers.size());

            } while (skip < totalUsers);

            userService.saveAllUsers(allUsers);
            userService.indexAllUsers();

            logger.info("Successfully loaded {} users from external API", allUsers.size());

        } catch (Exception e) {
            logger.error("Failed to load users from external API", e);
            throw new DataLoadException("Failed to load users from external API: " + e.getMessage(), e);
        }
    }

    private User convertToUser(DummyJsonUserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setSsn(dto.getSsn());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        user.setRole(dto.getRole());
        user.setPhone(dto.getPhone());
        user.setUsername(dto.getUsername());
        user.setBirthDate(dto.getBirthDate());
        user.setGender(dto.getGender());
        return user;
    }
}