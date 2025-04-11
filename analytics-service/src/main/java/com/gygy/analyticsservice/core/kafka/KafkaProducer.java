package com.gygy.analyticsservice.core.kafka;

import com.gygy.analyticsservice.core.configuration.KafkaTopicConfig;
import com.gygy.analyticsservice.event.PlanUsageEvent;
import com.gygy.analyticsservice.event.SystemMetricsEvent;
import com.gygy.analyticsservice.event.UserActivityEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka Producer for the analytics service.
 * Sends events to the respective Kafka topics.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicConfig kafkaTopicConfig;

    /**
     * Sends a user activity event to the user activity topic.
     * 
     * @param event User activity event to send
     */
    public void sendUserActivityEvent(UserActivityEvent event) {
        log.info("Sending user activity event: {}", event);
        kafkaTemplate.send(kafkaTopicConfig.getUserActivityTopic(), event.getUserId(), event);
    }

    /**
     * Sends a system metrics event to the system metrics topic.
     * 
     * @param event System metrics event to send
     */
    public void sendSystemMetricsEvent(SystemMetricsEvent event) {
        log.info("Sending system metrics event: {}", event);
        kafkaTemplate.send(kafkaTopicConfig.getSystemMetricsTopic(), event.getServiceName(), event);
    }

    /**
     * Sends a plan usage event to the plan usage topic.
     * 
     * @param event Plan usage event to send
     */
    public void sendPlanUsageEvent(PlanUsageEvent event) {
        log.info("Sending plan usage event: {}", event);
        kafkaTemplate.send(kafkaTopicConfig.getPlanUsageTopic(), event.getPlanId(), event);
    }
}