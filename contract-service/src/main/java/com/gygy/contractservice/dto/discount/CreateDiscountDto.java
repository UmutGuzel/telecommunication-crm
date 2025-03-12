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
public class CreateDiscountDto {
    @NotNull(message = "Contract detail ID is required")
    private UUID contractDetailId;
    @NotNull(message = "Billing Plan  ID is required")
    private UUID billingPlanId;

    @NotBlank(message = "Discount type is required")
    private String discountType;

    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    private double amount;
    @Min(value = 0, message = "Percentage must be greater than or equal to 0")
    private double percentage;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    @NotNull(message = "Description is required")
    private String description;
}
