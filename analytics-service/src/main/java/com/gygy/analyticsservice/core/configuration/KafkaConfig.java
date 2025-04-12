package com.gygy.analyticsservice.core.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.clients.admin.NewTopic;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import com.gygy.common.events.customersupportservice.TicketCreatedEvent;
import com.gygy.common.events.customersupportservice.TicketResponseEvent;
import com.gygy.common.events.customersupportservice.TicketStatusChangeEvent;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    private final KafkaTopic kafkaTopic;

    private <T> ConsumerFactory<String, T> createConsumerFactory(Class<T> clazz) {
        JsonDeserializer<T> deserializer = new JsonDeserializer<>(clazz);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> createFactory(Class<T> clazz) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createConsumerFactory(clazz));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TicketCreatedEvent> ticketCreatedKafkaListenerContainerFactory() {
        return createFactory(TicketCreatedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TicketResponseEvent> ticketResponseKafkaListenerContainerFactory() {
        return createFactory(TicketResponseEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TicketStatusChangeEvent> ticketStatusChangeKafkaListenerContainerFactory() {
        return createFactory(TicketStatusChangeEvent.class);
    }

    @Bean
    public NewTopic ticketCreatedTopic() {
        return new NewTopic(kafkaTopic.getTicketCreatedTopic(), 1, (short) 1);
    }

    @Bean
    public NewTopic ticketResponseTopic() {
        return new NewTopic(kafkaTopic.getTicketResponseTopic(), 1, (short) 1);
    }

    @Bean
    public NewTopic ticketStatusChangeTopic() {
        return new NewTopic(kafkaTopic.getTicketStatusChangeTopic(), 1, (short) 1);
    }

}
