package com.gygy.customerservice.infrastructure.outbox;

import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Transactional
    public void saveOutbox(String aggregateType, String aggregateId, String eventType, Object payload, String topic) {
        try {
            String payloadJson = objectMapper.writeValueAsString(payload);
            
            OutboxEntity outbox = OutboxEntity.builder()
                    .aggregateType(aggregateType)
                    .aggregateId(aggregateId)
                    .eventType(eventType)
                    .payload(payloadJson)
                    .topic(topic)
                    .status(OutboxStatus.PENDING)
                    .build();
            
            outboxRepository.save(outbox);
        } catch (JsonProcessingException e) {
            log.error("Error while saving outbox message", e);
            throw new RuntimeException("Error while saving outbox message", e);
        }
    }

    @Transactional
    public void processOutboxMessages() {
        List<OutboxEntity> messages = outboxRepository.findByStatusInOrderByCreatedAtAsc(
                List.of(OutboxStatus.PENDING, OutboxStatus.FAILED)
        );
        
        for (OutboxEntity message : messages) {
            try {
                EventType eventType = EventType.fromString(message.getEventType());
                Object event = objectMapper.readValue(message.getPayload(), eventType.getEventClass());
                
                kafkaTemplate.send(eventType.getTopic(), event);
                message.setStatus(OutboxStatus.PROCESSED);
                message.setProcessedAt(LocalDateTime.now());
                outboxRepository.save(message);
                log.info("Successfully processed outbox message: {}", message.getId());
            } catch (Exception e) {
                log.error("Error while processing outbox message: {}", message.getId(), e);
                message.setStatus(OutboxStatus.FAILED);
                outboxRepository.save(message);
            }
        }
    }

    @Transactional
    public void processOutboxMessage(OutboxEntity message) {
        try {
            EventType eventType = EventType.fromString(message.getEventType());
            Object event = objectMapper.readValue(message.getPayload(), eventType.getEventClass());

            kafkaTemplate.send(eventType.getTopic(), event);
            message.setStatus(OutboxStatus.PROCESSED);
            message.setProcessedAt(LocalDateTime.now());
            outboxRepository.save(message);
            log.info("Successfully processed outbox message: {}", message.getId());
        } catch (Exception e) {
            log.error("Error while processing outbox message: {}", message.getId(), e);
            message.setStatus(OutboxStatus.FAILED);
            outboxRepository.save(message);
        }
    }

    @Scheduled(fixedRate = 60000) // 1 dakikada bir çalışacak
    public void processFailedOutboxMessages() {
        log.info("Processing failed outbox messages...");
        List<OutboxEntity> failedMessages = outboxRepository.findByStatus(OutboxStatus.FAILED);

        for (OutboxEntity outbox : failedMessages) {
            try {
                processOutboxMessage(outbox);
            } catch (Exception e) {
                log.error("Failed to process outbox message: {}", outbox.getId(), e);
            }
        }
    }

    public void processOutboxEvent(String eventJson) {
        try {
            JsonNode eventNode = objectMapper.readTree(eventJson);
            JsonNode payload = eventNode.get("payload");

            if (payload != null) {
                String operation = payload.get("op").asText();
                if ("c".equals(operation)) { // Create operation
                    JsonNode after = payload.get("after");
                    if (after != null) {
                        String id = after.get("id").asText();
                        OutboxEntity outbox = outboxRepository.findById(UUID.fromString(id))
                                .orElseThrow(() -> new RuntimeException("Outbox message not found: " + id));

                        if (outbox.getStatus() == OutboxStatus.PENDING) {
                            processOutboxMessage(outbox);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error processing CDC event", e);
        }
    }

    private Class<?> getEventClass(String eventType) {
        return switch (eventType) {
            case "INDIVIDUAL_CUSTOMER_READ_CREATED" -> com.gygy.customerservice.infrastructure.messaging.event.db.CreatedIndividualCustomerReadEvent.class;
            default -> throw new IllegalArgumentException("Unknown event type: " + eventType);
        };
    }

    @Transactional
    public void markAsProcessed(UUID id) {
        OutboxEntity outbox = outboxRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Outbox event not found: " + id));
        
        outbox.setStatus(OutboxStatus.PROCESSED);
        outbox.setProcessedAt(LocalDateTime.now());
        outboxRepository.save(outbox);
        
        log.info("Outbox event marked as processed: {}", id);
    }

    @Transactional
    public void markAsFailed(UUID id) {
        OutboxEntity outbox = outboxRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Outbox event not found: " + id));
        
        outbox.setStatus(OutboxStatus.FAILED);
        outboxRepository.save(outbox);
        
        log.info("Outbox event marked as failed: {}", id);
    }
} 