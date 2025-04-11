package com.gygy.analyticsservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Event representing user activity in the system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityEvent {
    private String userId;
    private String username;
    private String activityType;
    private String activityDescription;
    private LocalDateTime timestamp;
    private String ipAddress;
    private String userAgent;
}