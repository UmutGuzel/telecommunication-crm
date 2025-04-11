package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;
import com.gygy.notificationservice.tempdto.IndividualCustomerUpdatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class IndividualCustomerUpdatedEventConsumer {

    private final EmailService emailService;
    private final KafkaTopicConfig kafkaTopicConfig;

    @KafkaListener(
            topics = "${kafka.topics.individualCustomerUpdatedTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "individualCustomerUpdatedKafkaListenerContainerFactory"
    )
    public void consume(IndividualCustomerUpdatedEvent event) {
        if (event == null) {
            log.error("Received null IndividualCustomerUpdatedEvent");
            return;
        }

        log.info("Received IndividualCustomerUpdatedEvent for: {}", event.getEmail());

        List<String> changed = event.getChangedFields();
        if (changed == null || changed.isEmpty()) {
            log.info("No changed fields found. Skipping email.");
            return;
        }

        List<String> notifyFields = List.of("email", "phoneNumber", "identityNumber");

        boolean shouldNotify = changed.stream().anyMatch(notifyFields::contains);
        if (!shouldNotify) {
            log.info("Changed fields are not important for email notification. Skipping.");
            return;
        }

        try {
            String subject = "Hesap Bilgileriniz Güncellendi";
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append(String.format("Merhaba %s %s,\n\nAşağıdaki bilgileriniz güncellendi:\n\n",
                    event.getName(), event.getSurname()));

            if (changed.contains("email")) {
                messageBuilder.append("Yeni e-posta: ").append(event.getEmail()).append("\n");
            }
            if (changed.contains("phoneNumber")) {
                messageBuilder.append("Yeni telefon: ").append(event.getPhoneNumber()).append("\n");
            }
            if (changed.contains("identityNumber")) {
                messageBuilder.append("Yeni kimlik numarası: ").append(event.getIdentityNumber()).append("\n");
            }

            messageBuilder.append("\nSaygılarımızla.");

            emailService.sendGenericEmail(event.getEmail(), subject, messageBuilder.toString());
            log.info("Update email sent to: {}", event.getEmail());

        } catch (Exception e) {
            log.error("Error sending update email to: {}", event.getEmail(), e);
        }
    }
}
