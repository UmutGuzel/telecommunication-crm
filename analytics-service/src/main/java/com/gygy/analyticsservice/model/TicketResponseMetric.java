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
@Document(collection = "ticket_response_metrics")
public class TicketResponseMetric {

    @Id
    private String id;

    private UUID ticketId;
    private UUID responseId;
    private LocalDateTime responseTimestamp;
    private Long responseTimeMinutes; // Time since previous response or ticket creation
    private int responseNumber; // First response = 1, etc.
}