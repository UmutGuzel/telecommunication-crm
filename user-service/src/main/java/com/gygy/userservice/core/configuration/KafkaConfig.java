package com.gygy.userservice.core.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import com.gygy.common.events.UserActivationEvent;
import com.gygy.common.events.PasswordResetEvent;

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
    public ProducerFactory<String, UserActivationEvent> userActivationProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getCommonConfig());
    }

    @Bean
    public ProducerFactory<String, PasswordResetEvent> passwordResetProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getCommonConfig());
    }

    @Bean
    public KafkaTemplate<String, UserActivationEvent> userActivationKafkaTemplate() {
        return new KafkaTemplate<>(userActivationProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, PasswordResetEvent> passwordResetKafkaTemplate() {
        return new KafkaTemplate<>(passwordResetProducerFactory());
    }
}