package com.gygy.customerservice.infrastructure.outbox;


import com.gygy.customerservice.infrastructure.messaging.event.db.CreatedIndividualCustomerReadEvent;

import java.util.Map;
import java.util.function.Supplier;

public enum EventType {
    INDIVIDUAL_CUSTOMER_READ_CREATED("individual-customer-read-created-topic", CreatedIndividualCustomerReadEvent.class);

    private final String topic;
    private final Class<?> eventClass;

    EventType(String topic, Class<?> eventClass) {
        this.topic = topic;
        this.eventClass = eventClass;
    }

    public String getTopic() {
        return topic;
    }

    public Class<?> getEventClass() {
        return eventClass;
    }

    public static EventType fromString(String eventType) {
        try {
            return EventType.valueOf(eventType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown event type: " + eventType);
        }
    }
} 