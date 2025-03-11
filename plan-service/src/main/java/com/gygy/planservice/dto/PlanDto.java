package com.gygy.planservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private CategoryDto category;
}