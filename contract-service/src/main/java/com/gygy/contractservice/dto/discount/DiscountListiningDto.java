package com.gygy.contractservice.dto.discount;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class DiscountListiningDto {
    private UUID contractDetailId;
    private UUID billingPlanId;
    private String discountType;
    private double amount;
    private double percentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
