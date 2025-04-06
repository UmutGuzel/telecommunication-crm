package com.gygy.notificationservice.consumer;

import com.gygy.common.events.paymentservice.bill.BillCreatedEvent;
import com.gygy.notificationservice.application.notification.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BillCreatedEventConsumer {

    private final EmailService emailService;

    @KafkaListener(
            topics = "${kafka.topics.billCreatedEventsTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(BillCreatedEvent event) {
        if (event == null) {
            log.error("Received null BillCreatedEvent");
            return;
        }

        log.info("Received BillCreatedEvent for customerId: {}", event.getCustomerId());

        try {
            String subject = "Yeni Faturanız Oluşturuldu";
            String message = String.format(
                    "Merhaba,\n\n%.2f TL tutarındaki yeni faturanız oluşturulmuştur.\nSon ödeme tarihi: %s.\n\nSaygılarımızla.",
                    event.getTotalAmount(),
                    event.getDueDate()
            );

            // customerId yerine gerçek e-posta kullanılacaksa event’e 'email' alanı eklenmeli
            emailService.sendGenericEmail(event.getCustomerId().toString(), subject, message);

            log.info("Bill created email sent to customerId: {}", event.getCustomerId());
        } catch (Exception e) {
            log.error("Error while processing BillCreatedEvent", e);
        }
    }
}
