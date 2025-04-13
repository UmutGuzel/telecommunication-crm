package com.gygy.customerservice.infrastructure.outbox;

import org.springframework.context.annotation.Configuration;

@Configuration
public class OutboxConfig {
    public static final int MAX_RETRY_ATTEMPTS = 3;
    public static final long RETRY_DELAY_MS = 1000L;
    public static final long KAFKA_SEND_TIMEOUT_SECONDS = 5L;
} 