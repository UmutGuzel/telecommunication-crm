package com.gygy.customerservice.infrastructure.outbox;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {
    private final OutboxService outboxService;

    // Scheduled task to process failed outbox messages every 30 seconds
    @Scheduled(fixedRate = 30000) // 30 seconds
    public void processOutboxMessages() {
        log.info("Starting failed outbox message processing...");
        outboxService.processOutboxMessages();
        log.info("Completed failed outbox message processing");
    }
} 