package com.gygy.customersupportservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDTO {
    private UUID id;
    private String message;
    private Long userId;
    private UUID ticketId;
    private LocalDateTime createdAt;
}