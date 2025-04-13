package com.gygy.customersupportservice.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.gygy.common.events.customersupportservice.TicketCreatedEvent;
import com.gygy.common.events.customersupportservice.TicketResponseEvent;
import com.gygy.common.events.customersupportservice.TicketStatusChangeEvent;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Map<String, Object> getCommonConfig() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configProps;
    }

    @Bean
    public ProducerFactory<String, TicketCreatedEvent> ticketCreatedProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getCommonConfig());
    }

    @Bean
    public KafkaTemplate<String, TicketCreatedEvent> ticketCreatedKafkaTemplate() {
        return new KafkaTemplate<>(ticketCreatedProducerFactory());
    }

    @Bean
    public ProducerFactory<String, TicketResponseEvent> ticketResponseProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getCommonConfig());
    }

    @Bean
    public KafkaTemplate<String, TicketResponseEvent> ticketResponseKafkaTemplate() {
        return new KafkaTemplate<>(ticketResponseProducerFactory());
    }

    @Bean
    public ProducerFactory<String, TicketStatusChangeEvent> ticketStatusChangeProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getCommonConfig());
    }

    @Bean
    public KafkaTemplate<String, TicketStatusChangeEvent> ticketStatusChangeKafkaTemplate() {
        return new KafkaTemplate<>(ticketStatusChangeProducerFactory());
    }
}