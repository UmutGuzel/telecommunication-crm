package com.gygy.customersupportservice.service;

import com.gygy.common.events.customersupportservice.TicketCreatedEvent;
import com.gygy.common.events.customersupportservice.TicketResponseEvent;
import com.gygy.common.events.customersupportservice.TicketStatusChangeEvent;
import com.gygy.customersupportservice.config.ApplicationConfig;
import com.gygy.customersupportservice.domain.Ticket;
import com.gygy.customersupportservice.domain.TicketResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {

    private final KafkaTemplate<String, TicketCreatedEvent> ticketCreatedKafkaTemplate;
    private final KafkaTemplate<String, TicketResponseEvent> ticketResponseKafkaTemplate;
    private final KafkaTemplate<String, TicketStatusChangeEvent> ticketStatusChangeKafkaTemplate;
    private final ApplicationConfig applicationConfig;

    public void publishTicketCreatedEvent(Ticket ticket) {
        TicketCreatedEvent event = TicketCreatedEvent.builder()
                .ticketId(ticket.getId())
                .customerId(ticket.getCustomerId())
                .userId(ticket.getUserId())
                .ticketTitle(ticket.getTitle())
                .ticketType(ticket.getType())
                .build();
        ticketCreatedKafkaTemplate.send(
                applicationConfig.getTicketCreatedTopic(),
                ticket.getId().toString(),
                event);
        log.info("Published ticket created event for ticket ID: {}", ticket.getId());
    }

    public void publishTicketResponseEvent(TicketResponse response) {
        TicketResponseEvent event = TicketResponseEvent.builder()
                .ticketId(response.getTicket().getId())
                .customerId(response.getTicket().getCustomerId())
                .userId(response.getTicket().getUserId())
                .ticketTitle(response.getTicket().getTitle())
                .ticketType(response.getTicket().getType())
                .responseId(response.getId())
                .build();
        ticketResponseKafkaTemplate.send(
                applicationConfig.getTicketResponseTopic(),
                response.getTicket().getId().toString(),
                event);
        log.info("Published ticket response event for ticket ID: {} and response ID: {}",
                response.getTicket().getId(), response.getId());
    }

    public void publishTicketStatusChangeEvent(Ticket ticket, String previousStatus) {
        TicketStatusChangeEvent event = TicketStatusChangeEvent.builder()
                .ticketId(ticket.getId())
                .customerId(ticket.getCustomerId())
                .userId(ticket.getUserId())
                .ticketTitle(ticket.getTitle())
                .ticketType(ticket.getType())
                .newStatus(ticket.getStatus().getStatus())
                .previousStatus(previousStatus)
                .build();
        ticketStatusChangeKafkaTemplate.send(
                applicationConfig.getTicketStatusChangeTopic(),
                ticket.getId().toString(),
                event);
        log.info("Published ticket status change event for ticket ID: {}, new status: {}",
                ticket.getId(), ticket.getStatus().getStatus());
    }
}