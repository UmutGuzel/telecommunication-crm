package com.gygy.analyticsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Analytics Service application main class.
 * This service collects and processes analytics data from various sources
 * and produces events to Kafka for further processing.
 */
@SpringBootApplication
@EnableMongoRepositories
public class AnalyticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsServiceApplication.class, args);
    }

}
