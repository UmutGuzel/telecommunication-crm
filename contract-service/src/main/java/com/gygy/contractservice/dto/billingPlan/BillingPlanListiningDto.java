package com.gygy.contractservice.dto.billingPlan;

import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.model.enums.BillingCycleType;
import com.gygy.contractservice.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class BillingPlanListiningDto {
    private UUID id;
    private String name;
    private String description;
    private String cycleType;
    private Integer billingDay;
    private Integer paymentDueDays;
    private BigDecimal baseAmount;
    private BigDecimal taxRate;
    private String paymentMethod;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
     private UUID contractDetailId;
}
