package com.gygy.planservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private UUID categoryId;
}