package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.notificationservice.tempdto.ContractDetailEvent; // ✅ doğru
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;

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

    @KafkaListener(
            topics = "${kafka.topics.contractDetailCreatedTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "contractDetailKafkaListenerContainerFactory"
    )
    public void consume(ContractDetailEvent event) {
        if (event == null) {
            log.error("Received null contract detail event");
            return;
        }

        log.info("Received contract detail event for customer: {}", event.getCustomerName());

        try {
            String subject = "Yeni Sözleşme Oluşturuldu";
            String message = String.format(
                    "Sayın %s,\n\nYeni sözleşmeniz oluşturulmuştur:\n\n" +
                            "- Sözleşme Adı: %s\n" +
                            "- İmza Tarihi: %s\n\n" +
                            "- İndirim: %s (%s)\n\n" +
                            "Sözleşmeniz hakkında daha fazla bilgi için bizimle iletişime geçebilirsiniz.\n\nSaygılarımızla.",
                    event.getCustomerName(),
                    event.getContractName(),
                    event.getSignatureDate(),
                    event.getDiscountName(),
                    event.getDiscountDescription()
            );

            emailService.sendGenericEmail(event.getEmail(), subject, message);
            log.info("Contract detail email sent to: {}", event.getEmail());

        } catch (Exception e) {
            log.error("Error sending contract detail email to: {}", event.getEmail(), e);
        }
    }
}
