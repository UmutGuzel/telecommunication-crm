package com.gygy.contractservice.dto.billingPlan;

import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.model.enums.BillingCycleType;
import com.gygy.contractservice.model.enums.PaymentMethod;
import com.gygy.contractservice.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class BillingPlanListiningDto {
    private String name;
    private String description;
    private BillingCycleType cycleType;

    private Integer baseAmount;
    private Double taxRate;
    private PaymentMethod paymentMethod;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
     private UUID contractId;

    public BillingPlanListiningDto(String name, String description, BillingCycleType cycleType,LocalDateTime createdAt,LocalDateTime updatedAt, Integer baseAmount, Contract contract, Status status, Double taxRate,PaymentMethod paymentMethod) {
        this.name = name;
        this.description = description;
        this.cycleType = cycleType;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
        this.baseAmount = baseAmount;
        this.taxRate = taxRate;
        this.status = status;
        this.contractId = contract.getId();
        this.paymentMethod=paymentMethod;
    }


    public BillingPlanListiningDto() {

    }



}
