package com.gygy.notificationservice.core.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Kafka yapılandırması, "notification-topic" adlı bir topic oluşturur.
 */
@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic notificationTopic() {
        return new NewTopic("notification-topic", 1, (short) 1);
    }
}