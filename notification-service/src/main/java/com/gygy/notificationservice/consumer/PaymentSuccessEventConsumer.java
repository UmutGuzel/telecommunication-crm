package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;
import com.gygy.notificationservice.tempdto.PaymentSuccessEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentSuccessEventConsumer {

    private final EmailService emailService;
    private final KafkaTopicConfig kafkaTopicConfig;

    @KafkaListener(
            topics = "${kafka.topics.paymentSuccessEventsTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "paymentSuccessKafkaListenerContainerFactory"
    )
    public void consume(PaymentSuccessEvent event) {
        if (event == null) {
            log.error("Received null PaymentSuccessEvent");
            return;
        }

        log.info("Received PaymentSuccessEvent for email: {}", event.getEmail());

        try {
            String subject = "Ödeme Başarıyla Gerçekleşti";
            String message = String.format(
                    "Merhaba,\n\n%.2f TL tutarındaki ödemeniz başarıyla alınmıştır.\n" +
                            "Ödeme Yöntemi: %s\n\nTeşekkür ederiz!\n\nSaygılarımızla.",
                    event.getPaidAmount(),
                    event.getPaymentMethod()
            );

            emailService.sendGenericEmail(event.getEmail(), subject, message);
            log.info("Payment success email sent to: {}", event.getEmail());

        } catch (Exception e) {
            log.error("Error processing PaymentSuccessEvent for email: {}", event.getEmail(), e);
        }
    }
}
