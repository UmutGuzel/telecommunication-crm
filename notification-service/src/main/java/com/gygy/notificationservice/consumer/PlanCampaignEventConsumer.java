package com.gygy.notificationservice.consumer;

import com.gygy.notificationservice.application.notification.EmailService;
import com.gygy.notificationservice.core.configuration.KafkaTopicConfig;
import com.gygy.notificationservice.tempdto.PlanCampaignEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PlanCampaignEventConsumer {

    private final EmailService emailService;
    private final KafkaTopicConfig kafkaTopicConfig;

    @KafkaListener(
            topics = "${kafka.topics.planCampaignTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "planCampaignKafkaListenerContainerFactory"
    )
    public void consume(PlanCampaignEvent event) {
        if (event == null) {
            log.error("Received null PlanCampaignEvent");
            return;
        }

        log.info("Received PlanCampaignEvent: {}", event.getCampaignName());

        try {
            String subject = "Yeni Kampanya Yayında!";
            String message = String.format(
                    "Merhaba,\n\nYeni bir kampanyamız var!\n\nKampanya: %s\nFiyat: %.2f₺\nAçıklama: %s\n\nKampanyayı kaçırmayın!",
                    event.getCampaignName(),
                    event.getPrice(),
                    event.getDescription()
            );

            // Kampanyayı herkese gönderiyormuş gibi örnekleme (test amaçlı e-mail adresi olabilir)
            emailService.sendGenericEmail("test@example.com", subject, message);
            log.info("Plan campaign email processed for campaign: {}", event.getCampaignName());

        } catch (Exception e) {
            log.error("Error sending campaign notification for: {}", event.getCampaignName(), e);
        }
    }
}
