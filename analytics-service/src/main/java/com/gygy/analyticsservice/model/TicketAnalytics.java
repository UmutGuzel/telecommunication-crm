package com.gygy.analyticsservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ticket_analytics")
public class TicketAnalytics {

    @Id
    private String id;

    private UUID ticketId;
    private UUID customerId;
    private UUID userId;
    private String ticketTitle;
    private String ticketType;
    private LocalDateTime createdAt;

    // Response metrics
    private int responseCount;
    private LocalDateTime firstResponseAt;
    private LocalDateTime lastResponseAt;
    private Long averageResponseTimeMinutes;

    // Status tracking
    private String currentStatus;
    private LocalDateTime lastStatusChangeAt;
    private int statusChangeCount;

    // Resolution metrics
    private boolean resolved;
    private LocalDateTime resolvedAt;
    private Long timeToResolutionMinutes;
}