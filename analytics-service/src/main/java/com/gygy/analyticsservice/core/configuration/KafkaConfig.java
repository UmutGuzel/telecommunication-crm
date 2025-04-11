package com.gygy.analyticsservice.core.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka configuration for the analytics service.
 * Sets up producer configuration and defines the Kafka topics.
 */
@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(JsonSerializer.TYPE_MAPPINGS,
                "userActivity:com.gygy.analyticsservice.event.UserActivityEvent," +
                        "systemMetrics:com.gygy.analyticsservice.event.SystemMetricsEvent," +
                        "planUsage:com.gygy.analyticsservice.event.PlanUsageEvent");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic userActivityTopic(@Value("${kafka.topics.userActivityTopic}") String topicName) {
        return new NewTopic(topicName, 1, (short) 1);
    }

    @Bean
    public NewTopic systemMetricsTopic(@Value("${kafka.topics.systemMetricsTopic}") String topicName) {
        return new NewTopic(topicName, 1, (short) 1);
    }

    @Bean
    public NewTopic planUsageTopic(@Value("${kafka.topics.planUsageTopic}") String topicName) {
        return new NewTopic(topicName, 1, (short) 1);
    }
}