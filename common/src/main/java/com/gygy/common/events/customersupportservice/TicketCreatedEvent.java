package com.gygy.common.events.customersupportservice;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
public class TicketCreatedEvent extends TicketAnalyticsEvent {
    @Builder
    public TicketCreatedEvent(UUID ticketId, String ticketTitle, String ticketType, Long customerId, Long userId) {
        super(ticketId, customerId, userId, ticketTitle, ticketType, LocalDateTime.now());
    }
}