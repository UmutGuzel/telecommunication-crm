package com.gygy.analyticsservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Event representing usage statistics of telecommunication plans.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanUsageEvent {
    private String planId;
    private String planName;
    private String userId;
    private String usageType; // DATA, VOICE, SMS
    private Double usageAmount;
    private String usageUnit; // MB, GB, MINUTES, COUNT
    private LocalDateTime timestamp;
}