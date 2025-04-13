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

@Slf4j
@Service
@RequiredArgsConstructor
public class DebeziumService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

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
            String eventType = (String) payload.get("event_type");
            String aggregateId = (String) payload.get("aggregate_id");
            String originalPayload = (String) payload.get("payload");
            String originalTopic = (String) payload.get("topic");
            String status = (String) payload.get("status");
            String operation = (String) payload.get("__op");
            
            if (eventType == null || aggregateId == null || originalPayload == null || originalTopic == null || status == null) {
                log.error("Missing required fields in payload - EventType: {}, AggregateId: {}, OriginalPayload: {}, OriginalTopic: {}, Status: {}", 
                    eventType, aggregateId, originalPayload, originalTopic, status);
                return;
            }
            
            // Only publish PENDING events
            if (!"PENDING".equals(status) || !"c".equals(operation)) {
                log.debug("Skipping non-PENDING event - Status: {}, Operation: {}", status, operation);
                return;
            }
            
            // Send to the original topic with the original payload
            log.info("Sending event to Kafka - Topic: {}, AggregateId: {}, EventType: {}", originalTopic, aggregateId, eventType);
            
            kafkaTemplate.send(originalTopic, aggregateId, originalPayload)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send event to Kafka - Topic: {}, Error: {}", originalTopic, ex.getMessage(), ex);
                    } else {
                        log.info("Successfully sent event to Kafka - Topic: {}", originalTopic);
                    }
                });
            
        } catch (Exception e) {
            log.error("Error processing CDC event: {}", e.getMessage(), e);
        }
    }

    private boolean sendEventWithRetry(String eventId, String topic, String key, String payload) {
        for(int attempt = 1; attempt <= 3; ++attempt) {
            try {
                log.info("Attempting to send event - EventId: {}, Topic: {}, Attempt: {}/{}", new Object[]{eventId, topic, attempt, 3});
                CompletableFuture<?> future = this.kafkaTemplate.send(topic, key, payload);
                future.get(5L, TimeUnit.SECONDS);
                log.info("Successfully sent event - EventId: {}, Topic: {}", eventId, topic);
                return true;
            } catch (Exception e) {
                log.error("Failed to send event - EventId: {}, Topic: {}, Attempt: {}/{}, Error: {}", new Object[]{eventId, topic, attempt, 3, e.getMessage()});
                if (attempt < 3) {
                    try {
                        Thread.sleep(1000L);
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