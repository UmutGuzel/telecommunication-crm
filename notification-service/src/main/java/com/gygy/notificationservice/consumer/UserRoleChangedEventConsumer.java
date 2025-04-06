package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;
import com.gygy.notificationservice.tempdto.UserRoleChangedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserRoleChangedEventConsumer {

    private final EmailService emailService;
    private final KafkaTopicConfig kafkaTopicConfig;

    @KafkaListener(
            topics = "${kafka.topics.userRoleChangedTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "userRoleChangedKafkaListenerContainerFactory"
    )
    public void consume(UserRoleChangedEvent event) {
        if (event == null) {
            log.error("Received null UserRoleChangedEvent");
            return;
        }

        log.info("Received UserRoleChangedEvent for email: {}", event.getEmail());

        try {
            String subject = "Kullanıcı Rolü Güncellendi";
            String rolesAsText = String.join(", ", event.getRoles());
            String message = String.format(
                    "Merhaba,\n\nHesabınıza atanmış roller güncellenmiştir.\nYeni rolleriniz: %s\n\nSaygılarımızla.",
                    rolesAsText
            );

            emailService.sendGenericEmail(event.getEmail(), subject, message);
            log.info("User role change email sent to: {}", event.getEmail());

        } catch (Exception e) {
            log.error("Error processing UserRoleChangedEvent for email: {}", event.getEmail(), e);
        }
    }
}
