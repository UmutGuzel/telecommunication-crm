package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;
import com.gygy.notificationservice.tempdto.PaymentFailedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentFailedEventConsumer {

    private final EmailService emailService;
    private final KafkaTopicConfig kafkaTopicConfig;

    @KafkaListener(
            topics = "${kafka.topics.paymentFailedEventsTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "paymentFailedKafkaListenerContainerFactory"
    )
    public void consume(PaymentFailedEvent event) {
        if (event == null) {
            log.error("Received null PaymentFailedEvent");
            return;
        }

        log.info("Received PaymentFailedEvent for email: {}", event.getEmail());

        try {
            String subject = "Ödeme Başarısız Oldu";
            String message = String.format(
                    "Merhaba,\n\n%.2f TL tutarındaki ödemeniz gerçekleştirilemedi.\n" +
                            "Sebep: %s\n\nLütfen tekrar deneyiniz veya bizimle iletişime geçiniz.\n\nSaygılarımızla.",
                    event.getPaidAmount(),
                    event.getReason()
            );

            emailService.sendGenericEmail(event.getEmail(), subject, message);
            log.info("Payment failure email sent to: {}", event.getEmail());

        } catch (Exception e) {
            log.error("Error processing PaymentFailedEvent for email: {}", event.getEmail(), e);
        }
    }
}
