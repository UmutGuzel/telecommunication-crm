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
@Builder
public class TicketStatusChangeEvent {
    private String newStatus;
    private String previousStatus;
    private UUID ticketId;
    private UUID customerId;
    private UUID userId;
    private String ticketTitle;
    private String ticketType;
    private LocalDateTime timestamp;
}