package com.gygy.customerservice.infrastructure.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {
    private final OutboxService outboxService;

    @Scheduled(fixedRate = 5000) // Her 5 saniyede bir çalışır
    public void processOutboxMessages() {
        log.debug("Processing outbox messages...");
        outboxService.processOutboxMessages();
    }
} 