package com.gygy.contractservice.dto.billingPlan;

import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.model.enums.BillingCycleType;
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
    private BillingCycleType cycleType;
    private Integer billingDay;
    private Integer paymentDueDays;
    private BigDecimal baseAmount;
    private BigDecimal taxRate;
    private String paymentMethod;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
     private ContractDetail contractDetail;

    public BillingPlanListiningDto(String name, String description, Integer billingDay, BillingCycleType cycleType, BigDecimal baseAmount, ContractDetail contractDetail, Integer paymentDueDays, String status, BigDecimal taxRate) {
        this.name = name;
        this.description = description;
        this.billingDay = billingDay;
        this.cycleType = cycleType;
        this.baseAmount = baseAmount;
        this.taxRate = taxRate;
        this.paymentDueDays = paymentDueDays;
        this.status = status;
        this.contractDetail = contractDetail;
    }





}
