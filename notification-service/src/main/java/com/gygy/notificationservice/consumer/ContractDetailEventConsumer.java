package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;
import com.gygy.common.events.ContractDetailEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ContractDetailEventConsumer {

    private final EmailService emailService;
    private final KafkaTopicConfig kafkaTopicConfig;

    @KafkaListener(topics = "${kafka.topics.contractDetailCreatedTopic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "contractDetailKafkaListenerContainerFactory")
    public void consume(ContractDetailEvent event) {
        if (event == null) {
            log.error("Received null contract detail event");
            return;
        }

        log.info("Received contract detail event for customer: {}", event.getCustomerName());

        try {
            String subject = "Sözleşme Detayları Oluşturuldu";
            String message = String.format(
                    "Sayın %s,\n\n'%s' adlı sözleşmenizin detayları %s tarihinde oluşturulmuştur.\nİndirim Oranı: %.2f%%.\n\nSaygılarımızla.",
                    event.getCustomerName(),
                    event.getContractName(),
                    event.getEventDate(),
                    event.getDiscount()
            );

            emailService.sendGenericEmail(event.getEmail(), subject, message);
            log.info("Contract detail email sent to: {}", event.getEmail());
        } catch (Exception e) {
            log.error("Error sending contract detail email to: {}", event.getEmail(), e);
        }
    }
}
