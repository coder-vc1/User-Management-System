package com.example.usermanagement.service;

import com.example.usermanagement.dto.UserResponseDto;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.UserNotFoundException;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.repository.UserSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    private final UserSearchRepository userSearchRepository;

    public UserService(UserRepository userRepository, UserSearchRepository userSearchRepository) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        logger.debug("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        logger.debug("Fetching user by id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return convertToDto(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserByEmail(String email) {
        logger.debug("Fetching user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return convertToDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> searchUsers(String searchTerm) {
        logger.debug("Searching users with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllUsers();
        }

        List<User> users;
        if (searchTerm.length() >= 3) {
            users = userSearchRepository.fullTextSearch(searchTerm.trim());
        } else {
            users = userRepository.findBySearchTerm(searchTerm.trim());
        }

        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void saveUser(User user) {
        logger.debug("Saving user: {}", user.getEmail());
        userRepository.save(user);
    }

    public void saveAllUsers(List<User> users) {
        logger.debug("Saving {} users", users.size());
        userRepository.saveAll(users);
    }

    public long getUserCount() {
        return userRepository.count();
    }

    public void indexAllUsers() {
        logger.debug("Indexing all users for search");
        userSearchRepository.indexAllUsers();
    }

    private UserResponseDto convertToDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getSsn(),
                user.getEmail(),
                user.getAge(),
                user.getRole(),
                user.getPhone(),
                user.getUsername(),
                user.getBirthDate(),
                user.getGender()
        );
    }
}