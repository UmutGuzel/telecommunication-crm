package com.gygy.contractservice.dto.billingPlan;

import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.model.enums.BillingCycleType;
import com.gygy.contractservice.model.enums.PaymentMethod;
import com.gygy.contractservice.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BillingPlanListiningDto {
    private String name;
    private String description;
    private BillingCycleType cycleType;
    // private Integer billingDay;
    // private Integer paymentDueDays;
    private Integer baseAmount;
    private Double taxRate;
    private PaymentMethod paymentMethod;
    private Status status;
    private UUID contractId;

    public BillingPlanListiningDto(String name, String description, BillingCycleType cycleType, Integer baseAmount,
            Contract contract, Status status, Double taxRate, PaymentMethod paymentMethod) {
        this.name = name;
        this.description = description;
        // this.billingDay = billingDay;
        this.cycleType = cycleType;
        this.baseAmount = baseAmount;
        this.taxRate = taxRate;
        // this.paymentDueDays = paymentDueDays;
        this.status = status;
        this.contractId = contract.getId();
        this.paymentMethod = paymentMethod;
    }

    public BillingPlanListiningDto() {

    }

}
