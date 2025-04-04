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
public class TicketDTO {
    private UUID id;
    private String title;
    private String description;
    private String type;
    private Long customerId;
    private Long userId;
    private String status;
    private String statusDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}