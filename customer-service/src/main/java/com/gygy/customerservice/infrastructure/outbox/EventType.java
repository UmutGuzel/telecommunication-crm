package com.gygy.customerservice.infrastructure.outbox;


import com.gygy.customerservice.infrastructure.messaging.event.db.CreatedCorporateCustomerReadEvent;
import com.gygy.customerservice.infrastructure.messaging.event.db.CreatedIndividualCustomerReadEvent;
import com.gygy.customerservice.infrastructure.messaging.event.db.UpdatedCorporateCustomerReadEvent;
import com.gygy.customerservice.infrastructure.messaging.event.db.UpdatedIndividualCustomerReadEvent;


public enum EventType {
    INDIVIDUAL_CUSTOMER_READ_CREATED("individual-customer-read-created-topic", CreatedIndividualCustomerReadEvent.class),
    INDIVIDUAL_CUSTOMER_READ_UPDATED("individual-customer-read-updated-topic", UpdatedIndividualCustomerReadEvent.class),
    CORPORATE_CUSTOMER_READ_CREATED("corporate-customer-read-created-topic", CreatedCorporateCustomerReadEvent.class),
    CORPORATE_CUSTOMER_READ_UPDATED("corporate-customer-read-updated-topic", UpdatedCorporateCustomerReadEvent.class);

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