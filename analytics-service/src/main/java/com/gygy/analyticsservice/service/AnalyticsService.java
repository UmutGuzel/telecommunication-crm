package com.gygy.analyticsservice.service;

import com.gygy.analyticsservice.core.kafka.KafkaProducer;
import com.gygy.analyticsservice.event.PlanUsageEvent;
import com.gygy.analyticsservice.event.SystemMetricsEvent;
import com.gygy.analyticsservice.event.UserActivityEvent;
import com.gygy.analyticsservice.model.UserActivity;
import com.gygy.analyticsservice.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service for handling analytics data and producing events.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsService {

    private final KafkaProducer kafkaProducer;
    private final UserActivityRepository userActivityRepository;

    /**
     * Tracks user activity and sends an event to Kafka.
     */
    public void trackUserActivity(String userId, String username, String activityType, String description,
            String ipAddress, String userAgent) {
        log.info("Tracking user activity: userId={}, activity={}", userId, activityType);

        // Create user activity record
        UserActivity activity = UserActivity.builder()
                .userId(userId)
                .username(username)
                .activityType(activityType)
                .description(description)
                .timestamp(LocalDateTime.now())
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();

        // Save to database
        userActivityRepository.save(activity);

        // Send event to Kafka
        UserActivityEvent event = UserActivityEvent.builder()
                .userId(userId)
                .username(username)
                .activityType(activityType)
                .activityDescription(description)
                .timestamp(LocalDateTime.now())
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();

        kafkaProducer.sendUserActivityEvent(event);
    }

    /**
     * Tracks system metrics and sends an event to Kafka.
     */
    public void trackSystemMetrics(String serviceName, String instanceId, String metricType,
            Double value, Map<String, Object> additionalMetrics) {
        log.info("Tracking system metrics: service={}, metric={}, value={}", serviceName, metricType, value);

        SystemMetricsEvent event = SystemMetricsEvent.builder()
                .serviceName(serviceName)
                .instanceId(instanceId)
                .metricType(metricType)
                .metricValue(value)
                .additionalMetrics(additionalMetrics)
                .timestamp(LocalDateTime.now())
                .build();

        kafkaProducer.sendSystemMetricsEvent(event);
    }

    /**
     * Tracks plan usage and sends an event to Kafka.
     */
    public void trackPlanUsage(String planId, String planName, String userId,
            String usageType, Double amount, String unit) {
        log.info("Tracking plan usage: planId={}, userId={}, type={}, amount={}", planId, userId, usageType, amount);

        PlanUsageEvent event = PlanUsageEvent.builder()
                .planId(planId)
                .planName(planName)
                .userId(userId)
                .usageType(usageType)
                .usageAmount(amount)
                .usageUnit(unit)
                .timestamp(LocalDateTime.now())
                .build();

        kafkaProducer.sendPlanUsageEvent(event);
    }

    /**
     * Gets user activities for a specific user.
     */
    public List<UserActivity> getUserActivities(String userId) {
        return userActivityRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    /**
     * Gets recent user activities across all users.
     */
    public List<UserActivity> getRecentActivities(int limit) {
        return userActivityRepository.findTop100ByOrderByTimestampDesc()
                .stream()
                .limit(limit)
                .toList();
    }
}