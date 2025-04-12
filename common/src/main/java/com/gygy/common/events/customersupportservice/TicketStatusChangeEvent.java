package com.gygy.common.events.customersupportservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketStatusChangeEvent extends TicketAnalyticsEvent {
    private String newStatus;
    private String previousStatus;

    @Builder
    public TicketStatusChangeEvent(UUID ticketId, String ticketTitle, String ticketType, Long customerId, Long userId,
            String newStatus, String previousStatus) {
        super(ticketId, customerId, userId, ticketTitle, ticketType, LocalDateTime.now());
        this.newStatus = newStatus;
        this.previousStatus = previousStatus;
    }
}