package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;
import com.gygy.notificationservice.tempdto.UserPermissionChangedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserPermissionChangedEventConsumer {

    private final EmailService emailService;
    private final KafkaTopicConfig kafkaTopicConfig;

    @KafkaListener(
            topics = "${kafka.topics.userPermissionChangedTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "userPermissionChangedKafkaListenerContainerFactory"
    )
    public void consume(UserPermissionChangedEvent event) {
        if (event == null) {
            log.error("Received null UserPermissionChangedEvent");
            return;
        }

        log.info("Received UserPermissionChangedEvent for email: {}", event.getEmail());

        try {
            String subject = "Yetkileriniz Güncellendi";
            String permissionsText = String.join(", ", event.getPermissions());
            String message = String.format(
                    "Merhaba,\n\nHesabınızın yetkileri güncellenmiştir.\nYeni yetkileriniz: %s\n\nSaygılarımızla.",
                    permissionsText
            );

            emailService.sendGenericEmail(event.getEmail(), subject, message);
            log.info("User permission change email sent to: {}", event.getEmail());

        } catch (Exception e) {
            log.error("Error processing UserPermissionChangedEvent for email: {}", event.getEmail(), e);
        }
    }
}
