package com.gygy.contractservice.dto.billingPlan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

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