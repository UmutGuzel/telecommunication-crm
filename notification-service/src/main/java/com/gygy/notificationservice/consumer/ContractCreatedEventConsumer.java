package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.tempdto.ContractCreatedEvent;
import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ContractCreatedEventConsumer {

    private final EmailService emailService;
    private final KafkaTopicConfig kafkaTopicConfig;

    @KafkaListener(
            topics = "${kafka.topics.contractCreatedEventTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ContractCreatedEvent event) {
        if (event == null) {
            log.error("Received null ContractCreatedEvent");
            return;
        }

        log.info("Received ContractCreatedEvent for contract: {}", event.getContractName());

        String subject = "Yeni Sözleşme Oluşturuldu";
        String message = String.format(
                "Sayın kullanıcımız,\n\n%s isimli sözleşmeniz %s tarihinde oluşturulmuştur.\n\nSaygılarımızla.",
                event.getContractName(), event.getStartDate());

        try {
            emailService.sendGenericEmail(event.getEmail(), subject, message);
        } catch (Exception e) {
            log.error("Error while sending contract created email to: {}", event.getEmail(), e);
        }
    }
}
