package com.gygy.customerservice.infrastructure.messaging;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaCustomerTopicConfig {

    @Bean
    public NewTopic customerCreatedTopic() {
        return TopicBuilder
                .name("customer-created-topic")
                .build();
    }


}
