package com.gygy.customersupportservice.config;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Configuration
@Getter
public class ApplicationConfig {
    @Value("${app-config.kafka.topics.ticket-created}")
    private String ticketCreatedTopic;

    @Value("${app-config.kafka.topics.ticket-response}")
    private String ticketResponseTopic;

    @Value("${app-config.kafka.topics.ticket-status-change}")
    private String ticketStatusChangeTopic;
}