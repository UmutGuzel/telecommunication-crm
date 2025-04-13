package com.gygy.debezium.service;

import java.util.Map;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.debezium.engine.ChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class DebeziumService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 1000;

    @Transactional
    public void handleEvent(ChangeEvent<String, String> event) {
        try {
            String key = event.key();
            String value = event.value();
            
            log.info("Received CDC event - Key: {}, Value: {}", key, value);
            
            // Parse the event value to get the payload
            Map<String, Object> eventMap = objectMapper.readValue(value, Map.class);
            log.debug("Parsed event map: {}", eventMap);
            
            Map<String, Object> payload = (Map<String, Object>) eventMap.get("payload");
            if (payload == null) {
                log.error("Payload is null in event: {}", eventMap);
                return;
            }
            
            // Get the event type and aggregate ID
            String eventId = (String) payload.get("id");
            String eventType = (String) payload.get("event_type");
            String aggregateId = (String) payload.get("aggregate_id");
            String originalPayload = (String) payload.get("payload");
            String originalTopic = (String) payload.get("topic");
            String status = (String) payload.get("status");
            String operation = (String) payload.get("__op");
            
            if (eventId == null || eventType == null || aggregateId == null || originalPayload == null || originalTopic == null || status == null) {
                log.error("Missing required fields in payload - EventId: {}, EventType: {}, AggregateId: {}, OriginalPayload: {}, OriginalTopic: {}, Status: {}", 
                    eventId, eventType, aggregateId, originalPayload, originalTopic, status);
                return;
            }
            
            // Only process PENDING events
            if (!"PENDING".equals(status) || !"c".equals(operation)) {
                log.debug("Skipping non-PENDING event - Status: {}, Operation: {}", status, operation);
                return;
            }
            
            sendEventWithRetry(eventId, originalTopic, aggregateId, originalPayload);
            
        } catch (Exception e) {
            log.error("Error processing CDC event: {}", e.getMessage(), e);
        }
    }

    private boolean sendEventWithRetry(String eventId, String topic, String key, String payload) {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                log.info("Attempting to send event - EventId: {}, Topic: {}, Attempt: {}/{}", 
                    eventId, topic, attempt, MAX_RETRIES);
                
                CompletableFuture<?> future = kafkaTemplate.send(topic, key, payload);
                future.get(5, TimeUnit.SECONDS); // Wait for the send to complete
                
                log.info("Successfully sent event - EventId: {}, Topic: {}", eventId, topic);
                return true;
                
            } catch (Exception e) {
                log.error("Failed to send event - EventId: {}, Topic: {}, Attempt: {}/{}, Error: {}", 
                    eventId, topic, attempt, MAX_RETRIES, e.getMessage());
                
                if (attempt < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        }
        return false;
    }
}