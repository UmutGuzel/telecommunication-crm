package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.common.events.PasswordResetEvent;
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PasswordResetEventConsumer {

    private final EmailService emailService;
    private final KafkaTopicConfig kafkaTopicConfig;

    @KafkaListener(topics = "${kafka.topics.passwordResetEventsTopic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "passwordResetKafkaListenerContainerFactory")
    public void consume(PasswordResetEvent event) {
        if (event == null) {
            log.error("Received null password reset event");
            return;
        }

        log.info("Received password reset event for user: {}", event.getUserId());

        try {
            emailService.sendPasswordResetEmail(event.getEmail(), event.getResetLink());
            log.info("Password reset email processing completed for user: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Error processing password reset event for user: {}",
                    event.getUserId() != null ? event.getUserId() : "unknown", e);
        }
    }
}