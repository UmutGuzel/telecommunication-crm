package com.gygy.contractservice.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaContractTopicConfig {
    @Bean
    public NewTopic contractTopic(){
        return TopicBuilder
                .name("contract_created_topic")
                .build();
    }

}
