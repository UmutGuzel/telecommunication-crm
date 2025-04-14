package com.gygy.common.events.customersupportservice;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketCreatedEvent {
    private UUID ticketId;
    private UUID customerId;
    private UUID userId;
    private String ticketTitle;
    private String ticketType;
    private LocalDateTime timestamp;
}