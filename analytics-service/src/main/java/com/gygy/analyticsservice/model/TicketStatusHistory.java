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
@Document(collection = "ticket_status_history")
public class TicketStatusHistory {

    @Id
    private String id;

    private UUID ticketId;
    private String previousStatus;
    private String newStatus;
    private LocalDateTime changedAt;
    private Long durationInPreviousStatusMinutes;
}