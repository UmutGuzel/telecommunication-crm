package com.gygy.contractservice.dto.billingPlan;

import com.gygy.contractservice.model.enums.BillingCycleType;
import com.gygy.contractservice.model.enums.PaymentMethod;
import com.gygy.contractservice.model.enums.Status;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class CreateBillingPlanDto {
    private UUID contract;
    private UUID planId;

    @NotNull(message = "Cycle type is required")
    private BillingCycleType cycleType;



    @NotNull(message = "Base amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Base amount must be at least 0")
    private Integer baseAmount;

    @NotNull(message = "Tax rate is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Tax rate must be at least 0")
    private Double taxRate;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Status is required")
    private Status status;


}
