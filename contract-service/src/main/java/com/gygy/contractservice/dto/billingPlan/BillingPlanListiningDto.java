package com.gygy.contractservice.dto.billingPlan;

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
    private UUID id;
    private String name;
    private String description;
    private BillingCycleType cycleType;
    private Integer billingDay;
    private Integer paymentDueDays;
    private Integer baseAmount;
    private Double taxRate;
    private PaymentMethod paymentMethod;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
     private ContractDetail contractDetail;

    public BillingPlanListiningDto(String name, String description, Integer billingDay, BillingCycleType cycleType,LocalDateTime createdAt,LocalDateTime updatedAt, Integer baseAmount, ContractDetail contractDetail, Integer paymentDueDays, Status status, Double taxRate) {
        this.name = name;
        this.description = description;
        this.billingDay = billingDay;
        this.cycleType = cycleType;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
        this.baseAmount = baseAmount;
        this.taxRate = taxRate;
        this.paymentDueDays = paymentDueDays;
        this.status = status;
        this.contractDetail = contractDetail;
    }


    public BillingPlanListiningDto() {

    }


}
