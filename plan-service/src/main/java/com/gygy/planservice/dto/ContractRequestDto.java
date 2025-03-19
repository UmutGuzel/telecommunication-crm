package com.gygy.planservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequestDto {
    @NotNull(message = "Type cannot be null")
    @NotBlank(message = "Type is required")
    @Size(min = 2, max = 50, message = "Type must be between 2 and 50 characters")
    private String type;

    @NotNull(message = "Discount cannot be null")
    @PositiveOrZero(message = "Discount must be zero or positive")
    private BigDecimal discount;

    @NotNull(message = "Plan ID cannot be null")
    private UUID planId;
}