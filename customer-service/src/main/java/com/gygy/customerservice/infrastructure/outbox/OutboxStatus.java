package com.gygy.customerservice.infrastructure.outbox;

public enum OutboxStatus {
    PENDING,
    PROCESSED,
    FAILED
} 