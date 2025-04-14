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
public class TicketReturnDTO {
    private String title;
    private String description;
    private String type;
    private UUID customerId;
    private String status;
    private LocalDateTime createdAt;
}
