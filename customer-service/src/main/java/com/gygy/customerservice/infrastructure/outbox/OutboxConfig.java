package com.gygy.customerservice.infrastructure.outbox;

import org.springframework.context.annotation.Configuration;

@Configuration
public class OutboxConfig {
    public static final int MAX_RETRY_ATTEMPTS = 5;
    public static final long INITIAL_RETRY_DELAY_MS = 1000L;
    public static final long MAX_RETRY_DELAY_MS = 30000L;
    public static final double RETRY_DELAY_MULTIPLIER = 2.0;
    public static final long KAFKA_SEND_TIMEOUT_SECONDS = 5L;
} 