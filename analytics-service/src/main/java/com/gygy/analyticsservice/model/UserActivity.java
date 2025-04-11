package com.gygy.analyticsservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * User activity model for storing in MongoDB.
 */
@Document(collection = "user_activities")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivity {
    @Id
    private String id;
    private String userId;
    private String username;
    private String activityType;
    private String description;
    private LocalDateTime timestamp;
    private String ipAddress;
    private String userAgent;
}