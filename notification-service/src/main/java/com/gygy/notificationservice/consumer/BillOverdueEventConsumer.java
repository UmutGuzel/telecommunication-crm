package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;
import com.gygy.notificationservice.tempdto.BillOverdueEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BillOverdueEventConsumer {

    private final EmailService emailService;
    private final KafkaTopicConfig kafkaTopicConfig;

    @KafkaListener(
            topics = "${kafka.topics.billOverdueEventsTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "billOverdueKafkaListenerContainerFactory"
    )
    public void consume(BillOverdueEvent event) {
        if (event == null) {
            log.error("Received null bill overdue event");
            return;
        }

        log.info("Received bill overdue event for email: {}", event.getEmail());

        try {
            String subject = "Fatura Gecikmesi Hakkında";
            String message = String.format(
                    "Merhaba,\n\n%.2f TL tutarındaki faturanızın son ödeme tarihi %s idi ve henüz ödenmedi.\n" +
                            "Lütfen en kısa sürede ödeme yapınız.\n\nSaygılarımızla.",
                    event.getAmount(),
                    event.getDueDate()
            );

            emailService.sendGenericEmail(event.getEmail(), subject, message);
            log.info("Overdue bill email sent to: {}", event.getEmail());

        } catch (Exception e) {
            log.error("Error processing BillOverdueEvent for email: {}", event.getEmail(), e);
        }
    }
}
