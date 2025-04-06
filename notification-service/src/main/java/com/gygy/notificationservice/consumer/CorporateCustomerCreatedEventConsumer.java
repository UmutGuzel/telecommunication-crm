package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;
import com.gygy.notificationservice.tempdto.CorporateCustomerCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CorporateCustomerCreatedEventConsumer {

    private final EmailService emailService;
    private final KafkaTopicConfig kafkaTopicConfig;

    @KafkaListener(
            topics = "${kafka.topics.corporateCustomerCreatedTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "corporateCustomerCreatedKafkaListenerContainerFactory"
    )
    public void consume(CorporateCustomerCreatedEvent event) {
        if (event == null) {
            log.error("Received null CorporateCustomerCreatedEvent");
            return;
        }

        try {
            String subject = "Kurumsal Hesabınız Oluşturuldu";
            String message = String.format(
                    "Merhaba %s %s,\n\n%s şirketine ait kurumsal müşteri kaydınız oluşturulmuştur.\nVergi Numarası: %s\n\nHoş geldiniz!",
                    event.getContactPersonName(),
                    event.getContactPersonSurname(),
                    event.getCompanyName(),
                    event.getTaxNumber()
            );

            emailService.sendGenericEmail(event.getEmail(), subject, message);
            log.info("Corporate creation email sent to: {}", event.getEmail());

        } catch (Exception e) {
            log.error("Error sending CorporateCustomerCreatedEvent email to: {}", event.getEmail(), e);
        }
    }
}
