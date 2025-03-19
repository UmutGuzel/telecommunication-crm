package com.gygy.contractservice.dto.billingPlan;

import com.gygy.contractservice.entity.Discount;
import com.gygy.contractservice.model.enums.BillingCycleType;
import com.gygy.contractservice.model.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UpdateBillingPlanDto {
    private UUID id;
    @NotNull(message = "Contract detail ID is required")
    private UUID contractDetailId;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Cycle type is required")
    private BillingCycleType cycleType;
    private List<Discount> discount;


    @NotNull(message = "Billing day is required")
    @Min(value = 1, message = "Billing day must be at least 1")
    private Integer billingDay;

    @NotNull(message = "Payment due days is required")
    @Min(value = 0, message = "Payment due days must be at least 0")
    private Integer paymentDueDays;

    @NotNull(message = "Base amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Base amount must be at least 0")
    private BigDecimal baseAmount;

    @NotNull(message = "Tax rate is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Tax rate must be at least 0")
    private BigDecimal taxRate;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Status is required")
    private String status;
}
