package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;
import com.gygy.notificationservice.tempdto.BillPaidEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BillPaidEventConsumer {

    private final EmailService emailService;
    private final KafkaTopicConfig kafkaTopicConfig;

    @KafkaListener(
            topics = "${kafka.topics.billPaidEventsTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "billPaidKafkaListenerContainerFactory"
    )
    public void consume(BillPaidEvent event) {
        if (event == null) {
            log.error("Received null bill paid event");
            return;
        }

        log.info("Received BillPaidEvent for email: {}", event.getEmail());

        try {
            String subject = "Ödemeniz Başarıyla Alındı";
            String message = String.format(
                    "Merhaba,\n\n%.2f TL tutarındaki ödemeniz %s tarihinde '%s' yöntemi ile alınmıştır.\n\n" +
                            "Teşekkür ederiz!\n\nSaygılarımızla.",
                    event.getTotalAmount(),
                    event.getPaymentDate(),
                    event.getPaymentMethod()
            );

            emailService.sendGenericEmail(event.getEmail(), subject, message);
            log.info("Payment confirmation email sent to: {}", event.getEmail());

        } catch (Exception e) {
            log.error("Error processing BillPaidEvent for email: {}", event.getEmail(), e);
        }
    }
}
