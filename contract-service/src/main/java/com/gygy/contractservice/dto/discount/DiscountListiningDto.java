package com.gygy.contractservice.dto.discount;

import com.gygy.contractservice.entity.BillingPlan;
import com.gygy.contractservice.entity.ContractDetail;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
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
    private  List<BillingPlan> billingPlanList;
    private LocalDate createDate;
    private LocalDate updateDate;

    public DiscountListiningDto(String discountType, double amount, double percentage, ContractDetail contractDetail, LocalDate endDate, LocalDate startDate, List<BillingPlan> billingPlans, LocalDate createdAt, LocalDate updatedAt) {
        this.discountType = discountType;
        this.amount = amount;
        this.percentage = percentage;
        this.contractDetailId = contractDetail.getId();
        this.endDate = endDate;
        this.startDate = startDate;
        this.billingPlanList = billingPlans;
        this.createDate = createdAt;
        this.updateDate = updatedAt;

    }
}
