package com.gygy.analyticsservice.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class KafkaTopic {
    @Value("${kafka.topics.ticket-created:ticket-created-events}")
    private String ticketCreatedTopic;

    @Value("${kafka.topics.ticket-response:ticket-response-events}")
    private String ticketResponseTopic;

    @Value("${kafka.topics.ticket-status-change:ticket-status-change-events}")
    private String ticketStatusChangeTopic;
}