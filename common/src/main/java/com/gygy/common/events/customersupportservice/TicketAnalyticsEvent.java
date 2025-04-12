package com.gygy.common.events.customersupportservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketAnalyticsEvent {
    private UUID ticketId;
    private Long customerId;
    private Long userId;
    private String ticketTitle;
    private String ticketType;
    private LocalDateTime timestamp;
}