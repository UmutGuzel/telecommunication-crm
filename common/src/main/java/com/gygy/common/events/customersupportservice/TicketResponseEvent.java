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
public class TicketResponseEvent extends TicketAnalyticsEvent {
    private UUID responseId;

    @Builder
    public TicketResponseEvent(UUID ticketId, String ticketTitle, String ticketType, Long customerId, Long userId,
            UUID responseId) {
        super(ticketId, customerId, userId, ticketTitle, ticketType, LocalDateTime.now());
        this.responseId = responseId;
    }
}