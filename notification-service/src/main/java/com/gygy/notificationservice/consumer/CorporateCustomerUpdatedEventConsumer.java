package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;
import com.gygy.notificationservice.tempdto.CorporateCustomerUpdatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CorporateCustomerUpdatedEventConsumer {

    private final EmailService emailService;
    private final KafkaTopicConfig kafkaTopicConfig;

    @KafkaListener(
            topics = "${kafka.topics.corporateCustomerUpdatedTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "corporateCustomerUpdatedKafkaListenerContainerFactory"
    )
    public void consume(CorporateCustomerUpdatedEvent event) {
        if (event == null) {
            log.error("Received null CorporateCustomerUpdatedEvent");
            return;
        }

        List<String> changed = event.getChangedFields();
        if (changed == null || changed.isEmpty()) {
            log.info("No changed fields found. Skipping email.");
            return;
        }

        List<String> notifyFields = List.of("email", "phoneNumber", "taxNumber");

        boolean shouldNotify = changed.stream().anyMatch(notifyFields::contains);
        if (!shouldNotify) {
            log.info("Changed fields do not require notification. Skipping mail for: {}", event.getEmail());
            return;
        }

        try {
            String subject = "Kurumsal Hesap Bilgileriniz Güncellendi";
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append(String.format("Merhaba %s %s,\n\nAşağıdaki bilgileriniz güncellendi:\n\n",
                    event.getContactPersonName(), event.getContactPersonSurname()));

            if (changed.contains("email")) {
                messageBuilder.append("Yeni e-posta: ").append(event.getEmail()).append("\n");
            }
            if (changed.contains("phoneNumber")) {
                messageBuilder.append("Yeni telefon: ").append(event.getPhoneNumber()).append("\n");
            }
            if (changed.contains("taxNumber")) {
                messageBuilder.append("Yeni vergi numarası: ").append(event.getTaxNumber()).append("\n");
            }

            messageBuilder.append("\nSaygılarımızla.");

            emailService.sendGenericEmail(event.getEmail(), subject, messageBuilder.toString());
            log.info("Corporate update email sent to: {}", event.getEmail());

        } catch (Exception e) {
            log.error("Error sending CorporateCustomerUpdatedEvent email to: {}", event.getEmail(), e);
        }
    }
}
