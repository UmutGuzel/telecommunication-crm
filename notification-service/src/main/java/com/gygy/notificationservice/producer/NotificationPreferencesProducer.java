package com.gygy.notificationservice.producer;

import com.gygy.notificationservice.tempdto.NotificationPreferencesChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationPreferencesProducer {

    private final KafkaTemplate<String, NotificationPreferencesChangedEvent> kafkaTemplate;

    public void send(NotificationPreferencesChangedEvent event) {
        kafkaTemplate.send("notification-preferences-changed-topic", event.getId().toString(), event);
        log.info("Notification preferences event sent for customer ID: {}", event.getId());
    }
}
