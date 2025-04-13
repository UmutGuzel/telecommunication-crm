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

    // @Scheduled(fixedRate = 5000) // Her 5 saniyede bir çalışır
    // public void processOutboxMessages() {
    //     log.debug("Processing outbox messages...");
    //     outboxService.processOutboxMessages();
    // }

    @Scheduled(fixedRate = 30000) // Her 30 saniyede bir çalışır
    public void processFailedEvents() {
        log.debug("Processing failed outbox messages...");
        outboxService.processFailedOutboxMessages();
    }
} 