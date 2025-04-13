package com.gygy.customerservice.infrastructure.outbox;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {
    private final OutboxService outboxService;

    // @Scheduled(fixedRate = 5000) // Her 5 saniyede bir çalışır
    public void processOutboxMessages() {
        log.debug("Processing outbox messages...");
        outboxService.processOutboxMessages();
    }
} 