package com.gygy.common.kafka.producer;

import com.gygy.common.events.base.BaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventProducer {

    private final StreamBridge streamBridge;

    public void sendEvent(String topic, BaseEvent event) {
        try {
            streamBridge.send(topic, event);
            log.info("Event sent successfully to topic: {}, event: {}", topic, event);
        } catch (Exception e) {
            log.error("Error sending event to topic: {}, event: {}", topic, event, e);
            throw new RuntimeException("Failed to send event to Kafka", e);
        }
    }
}