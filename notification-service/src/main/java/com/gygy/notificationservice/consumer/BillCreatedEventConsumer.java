package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;
import com.gygy.notificationservice.tempdto.BillCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BillCreatedEventConsumer {

    private final EmailService emailService;
    private final KafkaTopicConfig kafkaTopicConfig;

    @KafkaListener(
            topics = "${kafka.topics.billCreatedEventsTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "billCreatedKafkaListenerContainerFactory"
    )
    public void consume(BillCreatedEvent event) {
        if (event == null) {
            log.error("Received null bill created event");
            return;
        }

        log.info("Received bill created event for email: {}", event.getEmail());

        try {
            emailService.sendGenericEmail(
                    event.getEmail(),
                    "Yeni Faturanız Oluşturuldu",
                    String.format("Merhaba,\n\n%.2f TL tutarındaki yeni faturanız oluşturulmuştur.\nSon ödeme tarihi: %s.\n\nSaygılarımızla.",
                            event.getTotalAmount(),
                            event.getDueDate()
                    )
            );

            log.info("Bill created email processing completed for email: {}", event.getEmail());
        } catch (Exception e) {
            log.error("Error processing bill created event for email: {}",
                    event.getEmail() != null ? event.getEmail() : "unknown", e);
        }
    }
}
