package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.common.events.UserActivationEvent;
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserActivationEventConsumer {

    private final EmailService emailService;
    private final KafkaTopicConfig kafkaTopicConfig;

    @KafkaListener(topics = "${kafka.topics.userActivationEventsTopic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "userActivationKafkaListenerContainerFactory")
    public void consume(UserActivationEvent event) {
        if (event == null) {
            log.error("Received null user activation event");
            return;
        }

        log.info("Received user activation event for email: {}", event.getEmail());

        try {
            emailService.sendActivationEmail(event.getEmail(), event.getName(), event.getActivationLink());
            log.info("User activation email processing completed for email: {}", event.getEmail());
        } catch (Exception e) {
            log.error("Error processing user activation event for email: {}",
                    event.getEmail() != null ? event.getEmail() : "unknown", e);
        }
    }
}