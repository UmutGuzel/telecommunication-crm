package com.gygy.discountservice.consumer;

import com.gygy.contractservice.event.DiscountEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(Sink.class)
public class DiscountEventConsumer {

    @StreamListener(Sink.INPUT)
    public void handleDiscountEvent(DiscountEvent event) {
        log.info("Received discount event: {}", event);
        
        // Process the discount event
        switch (event.getEventType()) {
            case "DISCOUNT_APPLIED":
                handleDiscountApplied(event);
                break;
            default:
                log.warn("Unknown event type: {}", event.getEventType());
        }
    }
    
    private void handleDiscountApplied(DiscountEvent event) {
        // Implement discount processing logic here
        log.info("Processing discount application for customer: {}", event.getCustomerId());
        // Add your business logic here
    }
} 