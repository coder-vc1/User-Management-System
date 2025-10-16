package com.example.usermanagement.controller;

import com.example.usermanagement.service.DataLoadService;
import com.example.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
@Tag(name = "Data Management", description = "APIs for loading and managing external data")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class DataLoadController {

    private static final Logger logger = LoggerFactory.getLogger(DataLoadController.class);

    private final DataLoadService dataLoadService;
    private final UserService userService;

    public DataLoadController(DataLoadService dataLoadService, UserService userService) {
        this.dataLoadService = dataLoadService;
        this.userService = userService;
    }

    @PostMapping("/load")
    @Operation(summary = "Load users from external API", 
               description = "Load all user data from DummyJSON API into the local H2 database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data loaded successfully"),
            @ApiResponse(responseCode = "500", description = "Error occurred while loading data")
    })
    public ResponseEntity<Map<String, Object>> loadUsersData() {
        logger.info("Request received to load users from external API");
        
        try {
            long beforeCount = userService.getUserCount();
            dataLoadService.loadUsersFromExternalAPI();
            long afterCount = userService.getUserCount();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Users data loaded successfully");
            response.put("previousCount", beforeCount);
            response.put("currentCount", afterCount);
            response.put("loadedCount", afterCount - beforeCount);
            
            logger.info("Successfully loaded users. Previous count: {}, Current count: {}", beforeCount, afterCount);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error loading users data", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error loading users data: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/status")
    @Operation(summary = "Get data load status", description = "Get the current status of loaded data")
    @ApiResponse(responseCode = "200", description = "Status retrieved successfully")
    public ResponseEntity<Map<String, Object>> getDataStatus() {
        logger.info("Request received to get data status");
        
        long userCount = userService.getUserCount();
        
        Map<String, Object> response = new HashMap<>();
        response.put("totalUsers", userCount);
        response.put("dataLoaded", userCount > 0);
        
        return ResponseEntity.ok(response);
    }
}