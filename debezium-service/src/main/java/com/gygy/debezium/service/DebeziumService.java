package com.gygy.debezium.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.debezium.engine.ChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class DebeziumService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final long RETRY_DELAY_MS = 1000L;

    public void handleEvent(ChangeEvent<String, String> event) {
        try {
            String key = event.key();
            String value = event.value();
            
            log.info("=== START PROCESSING CDC EVENT ===");
            log.info("Raw Event Key: {}", key);
            log.info("Raw Event Value: {}", value);
            
            // Parse the event value to get the payload
            Map<String, Object> eventMap = objectMapper.readValue(value, Map.class);
            log.info("Parsed Event Map: {}", eventMap);
            
            Map<String, Object> payload = (Map<String, Object>) eventMap.get("payload");
            if (payload == null) {
                log.error("Payload is null in event: {}", eventMap);
                return;
            }
            
            // Get the event type and aggregate ID
            String eventType = (String) payload.get("event_type");
            String aggregateId = (String) payload.get("aggregate_id");
            String originalPayload = (String) payload.get("payload");
            String originalTopic = (String) payload.get("topic");
            String status = (String) payload.get("status");
            String operation = (String) payload.get("__op");
            
            log.info("Event Details:");
            log.info("Event Type: {}", eventType);
            log.info("Aggregate ID: {}", aggregateId);
            log.info("Original Topic: {}", originalTopic);
            log.info("Status: {}", status);
            log.info("Operation: {}", operation);
            
            if (eventType == null || aggregateId == null || originalPayload == null || originalTopic == null || status == null) {
                log.error("Missing required fields in payload - EventType: {}, AggregateId: {}, OriginalPayload: {}, OriginalTopic: {}, Status: {}", 
                    eventType, aggregateId, originalPayload, originalTopic, status);
                return;
            }
            
            // Only publish PENDING events
            if (!"PENDING".equals(status) || !"c".equals(operation)) {
                log.info("Skipping non-PENDING event - Status: {}, Operation: {}", status, operation);
                return;
            }
            
            // Send to the original topic with the original payload
            log.info("Attempting to send event to Kafka - Topic: {}, AggregateId: {}, EventType: {}", originalTopic, aggregateId, eventType);

            boolean success = sendEventWithRetry(aggregateId, originalTopic, aggregateId, originalPayload);
            if (success) {
                log.info("Event successfully sent to Kafka");
                // If event is successfully sent, mark as processed
                log.info("Marking event as processed - ID: {}", aggregateId);
                restTemplate.put("http://localhost:9020/api/outbox/{id}/mark-as-processed", null, aggregateId);
            } else {
                log.error("Failed to send event to Kafka");
                // If event sending failed, mark as failed
                log.info("Marking event as failed - ID: {}", aggregateId);
                restTemplate.put("http://localhost:9020/api/outbox/{id}/mark-as-failed", null, aggregateId);
            }
            
            log.info("=== END PROCESSING CDC EVENT ===");
        } catch (Exception e) {
            log.error("Error processing CDC event: {}", e.getMessage(), e);
        }
    }

    private boolean sendEventWithRetry(String eventId, String topic, String key, String payload) {
        for(int attempt = 1; attempt <= MAX_RETRY_ATTEMPTS; ++attempt) {
            try {
                log.info("=== START KAFKA SEND ATTEMPT ===");
                log.info("EventId: {}", eventId);
                log.info("Topic: {}", topic);
                log.info("Key: {}", key);
                log.info("Payload: {}", payload);
                log.info("Attempt: {}/{}", attempt, MAX_RETRY_ATTEMPTS);
                
                CompletableFuture<?> future = this.kafkaTemplate.send(topic, key, payload);
                log.info("Kafka send future created, waiting for completion...");
                
                future.get(5L, TimeUnit.SECONDS);
                log.info("Kafka send completed successfully");
                log.info("=== END KAFKA SEND ATTEMPT ===");
                return true;
            } catch (Exception e) {
                log.error("=== KAFKA SEND FAILED ===");
                log.error("Error details: {}", e.getMessage());
                log.error("Stack trace: ", e);
                log.error("=== END KAFKA SEND FAILED ===");
                
                if (attempt < MAX_RETRY_ATTEMPTS) {
                    try {
                        log.info("Retrying in {} ms...", RETRY_DELAY_MS);
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException var8) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        }
        return false;
    }

} 