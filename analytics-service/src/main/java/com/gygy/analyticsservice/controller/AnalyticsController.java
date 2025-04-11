package com.gygy.analyticsservice.controller;

import com.gygy.analyticsservice.model.UserActivity;
import com.gygy.analyticsservice.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for the analytics service.
 * Provides endpoints for tracking and retrieving analytics data.
 */
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * Track user activity.
     */
    @PostMapping("/track/user-activity")
    public ResponseEntity<Void> trackUserActivity(@RequestBody Map<String, Object> activityData) {
        log.info("Received user activity tracking request");

        String userId = (String) activityData.get("userId");
        String username = (String) activityData.get("username");
        String activityType = (String) activityData.get("activityType");
        String description = (String) activityData.get("description");
        String ipAddress = (String) activityData.get("ipAddress");
        String userAgent = (String) activityData.get("userAgent");

        analyticsService.trackUserActivity(userId, username, activityType, description, ipAddress, userAgent);

        return ResponseEntity.ok().build();
    }

    /**
     * Track system metrics.
     */
    @PostMapping("/track/system-metrics")
    public ResponseEntity<Void> trackSystemMetrics(@RequestBody Map<String, Object> metricsData) {
        log.info("Received system metrics tracking request");

        String serviceName = (String) metricsData.get("serviceName");
        String instanceId = (String) metricsData.get("instanceId");
        String metricType = (String) metricsData.get("metricType");
        Double value = (Double) metricsData.get("value");

        @SuppressWarnings("unchecked")
        Map<String, Object> additionalMetrics = (Map<String, Object>) metricsData.get("additionalMetrics");

        analyticsService.trackSystemMetrics(serviceName, instanceId, metricType, value, additionalMetrics);

        return ResponseEntity.ok().build();
    }

    /**
     * Track plan usage.
     */
    @PostMapping("/track/plan-usage")
    public ResponseEntity<Void> trackPlanUsage(@RequestBody Map<String, Object> usageData) {
        log.info("Received plan usage tracking request");

        String planId = (String) usageData.get("planId");
        String planName = (String) usageData.get("planName");
        String userId = (String) usageData.get("userId");
        String usageType = (String) usageData.get("usageType");
        Double amount = (Double) usageData.get("amount");
        String unit = (String) usageData.get("unit");

        analyticsService.trackPlanUsage(planId, planName, userId, usageType, amount, unit);

        return ResponseEntity.ok().build();
    }

    /**
     * Get user activities for a specific user.
     */
    @GetMapping("/user-activities/{userId}")
    public ResponseEntity<List<UserActivity>> getUserActivities(@PathVariable String userId) {
        log.info("Getting user activities for user ID: {}", userId);
        return ResponseEntity.ok(analyticsService.getUserActivities(userId));
    }

    /**
     * Get recent activities across all users.
     */
    @GetMapping("/recent-activities")
    public ResponseEntity<List<UserActivity>> getRecentActivities(
            @RequestParam(defaultValue = "20") int limit) {
        log.info("Getting recent activities, limit: {}", limit);
        return ResponseEntity.ok(analyticsService.getRecentActivities(limit));
    }
}