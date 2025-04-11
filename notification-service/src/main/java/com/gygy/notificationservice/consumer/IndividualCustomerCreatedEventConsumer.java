package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;
import com.gygy.notificationservice.tempdto.IndividualCustomerCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class IndividualCustomerCreatedEventConsumer {

    private final EmailService emailService;
    private final KafkaTopicConfig kafkaTopicConfig;

    @KafkaListener(
            topics = "${kafka.topics.individualCustomerCreatedTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "individualCustomerCreatedKafkaListenerContainerFactory"
    )
    public void consume(IndividualCustomerCreatedEvent event) {
        if (event == null) {
            log.error("Received null IndividualCustomerCreatedEvent");
            return;
        }

        log.info("Received IndividualCustomerCreatedEvent for: {}", event.getEmail());

        try {
            String subject = "Hesabınız Oluşturuldu";
            String message = String.format(
                    "Merhaba %s %s,\n\nHesabınız başarıyla oluşturulmuştur.\nID: %s\n\nHoş geldiniz!",
                    event.getName(),
                    event.getSurname(),
                    event.getId()
            );

            emailService.sendGenericEmail(event.getEmail(), subject, message);
            log.info("Customer creation email sent to: {}", event.getEmail());

        } catch (Exception e) {
            log.error("Error sending IndividualCustomerCreatedEvent email to: {}", event.getEmail(), e);
        }
    }
}
