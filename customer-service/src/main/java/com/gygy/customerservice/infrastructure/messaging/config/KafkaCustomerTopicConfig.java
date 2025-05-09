package com.gygy.customerservice.infrastructure.messaging.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaCustomerTopicConfig {

    @Bean
    public NewTopic individualCustomerCreatedTopic() {
        return TopicBuilder
                .name("individual-customer-created-topic")
                .build();
    }

    @Bean
    public NewTopic corporateCustomerCreatedTopic() {
        return TopicBuilder
                .name("corporate-customer-created-topic")
                .build();
    }

    @Bean
    public NewTopic individualCustomerUpdatedTopic() {
        return TopicBuilder
                .name("individual-customer-updated-topic")
                .build();
    }

    @Bean
    public NewTopic corporateCustomerUpdatedTopic() {
        return TopicBuilder
                .name("corporate-customer-updated-topic")
                .build();
    }
    
    @Bean
    public NewTopic individualCustomerReadCreatedTopic() {
        return TopicBuilder
                .name("individual-customer-read-created-topic")
                .build();
    }

    @Bean
    public NewTopic individualCustomerReadUpdatedTopic() {
        return TopicBuilder
                .name("individual-customer-read-updated-topic")
                .build();
    }

    @Bean
    public NewTopic corporateCustomerReadCreatedTopic() {
        return TopicBuilder
                .name("corporate-customer-read-created-topic")
                .build();
    }

    @Bean
    public NewTopic corporateCustomerReadUpdatedTopic() {
        return TopicBuilder
                .name("corporate-customer-read-updated-topic")
                .build();
    }
}
