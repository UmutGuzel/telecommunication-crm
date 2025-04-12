package com.gygy.analyticsservice.consumer;

import com.gygy.analyticsservice.service.TicketAnalyticsService;
import com.gygy.common.events.customersupportservice.TicketCreatedEvent;
import com.gygy.common.events.customersupportservice.TicketResponseEvent;
import com.gygy.common.events.customersupportservice.TicketStatusChangeEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketEventConsumer {

    private final TicketAnalyticsService ticketAnalyticsService;

    @KafkaListener(topics = "${kafka.topics.ticket-created:ticket-created-events}", containerFactory = "ticketCreatedKafkaListenerContainerFactory", groupId = "analytics-ticket-created")
    public void consumeTicketCreatedEvent(TicketCreatedEvent event) {
        log.info("Received ticket created event: {}", event);
        try {
            ticketAnalyticsService.processTicketCreatedEvent(event);
        } catch (Exception e) {
            log.error("Error processing ticket created event: {}", event, e);
        }
    }

    @KafkaListener(topics = "${kafka.topics.ticket-response:ticket-response-events}", containerFactory = "ticketResponseKafkaListenerContainerFactory", groupId = "analytics-ticket-response")
    public void consumeTicketResponseEvent(TicketResponseEvent event) {
        log.info("Received ticket response event: {}", event);
        try {
            ticketAnalyticsService.processTicketResponseEvent(event);
        } catch (Exception e) {
            log.error("Error processing ticket response event: {}", event, e);
        }
    }

    @KafkaListener(topics = "${kafka.topics.ticket-status-change:ticket-status-change-events}", containerFactory = "ticketStatusChangeKafkaListenerContainerFactory", groupId = "analytics-ticket-status-change")
    public void consumeTicketStatusChangeEvent(TicketStatusChangeEvent event) {
        log.info("Received ticket status change event: {}", event);
        try {
            ticketAnalyticsService.processTicketStatusChangeEvent(event);
        } catch (Exception e) {
            log.error("Error processing ticket status change event: {}", event, e);
        }
    }
}