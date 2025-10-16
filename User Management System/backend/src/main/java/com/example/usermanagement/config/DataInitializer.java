package com.example.usermanagement.config;

import com.example.usermanagement.service.DataLoadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final DataLoadService dataLoadService;

    public DataInitializer(DataLoadService dataLoadService) {
        this.dataLoadService = dataLoadService;
    }

    @Override
    public void run(String... args) {
        logger.info("Starting application data initialization");
        try {
            dataLoadService.loadUsersFromExternalAPI();
        } catch (Exception e) {
            logger.warn("Failed to load initial data automatically. Data can be loaded via /api/data/load endpoint", e);
        }
    }
}