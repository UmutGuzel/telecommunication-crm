package com.gygy.contractservice.mapper;

import com.gygy.contractservice.dto.billingPlan.BillingPlanListiningDto;
import com.gygy.contractservice.dto.billingPlan.CreateBillingPlanDto;
import com.gygy.contractservice.dto.billingPlan.UpdateBillingPlanDto;
import com.gygy.contractservice.entity.BillingPlan;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
public class BillingPlanMapper {
    public BillingPlan createBillingPlanFromCreateBillingPlanDto(CreateBillingPlanDto createBillingPlanDto){
        return BillingPlan.builder()
                .createdAt(LocalDateTime.now())
                .baseAmount(createBillingPlanDto.getBaseAmount())
                .cycleType(createBillingPlanDto.getCycleType())
                .status(createBillingPlanDto.getStatus())
                .updatedAt(LocalDateTime.now())
                .taxRate(createBillingPlanDto.getTaxRate())
                .paymentMethod(createBillingPlanDto.getPaymentMethod())
                .planId(createBillingPlanDto.getPlanId())
                .build();

    }
    public BillingPlan updateBillingPlanFromUpdateBillingPlanDto(UpdateBillingPlanDto updateBillingPlanDto){
        return BillingPlan.builder()
                .taxRate(updateBillingPlanDto.getTaxRate())
                .updatedAt(LocalDateTime.now())
                .cycleType(updateBillingPlanDto.getCycleType())
                .id(updateBillingPlanDto.getId())
                .status(updateBillingPlanDto.getStatus())
                .description(updateBillingPlanDto.getDescription())
                .baseAmount(updateBillingPlanDto.getBaseAmount())
                .name(updateBillingPlanDto.getName())
                .createdAt(LocalDateTime.now())
                .paymentMethod(updateBillingPlanDto.getPaymentMethod())
                .build();
    }
    public BillingPlanListiningDto toBillingPlanListiningDto(BillingPlan billingPlan){
        return new BillingPlanListiningDto(
                billingPlan.getName()
                ,billingPlan.getDescription()
                ,billingPlan.getCycleType()
                ,billingPlan.getCreatedAt(),
                billingPlan.getUpdatedAt()
                ,billingPlan.getBaseAmount()
                ,billingPlan.getContract()
                ,billingPlan.getStatus()
                ,billingPlan.getTaxRate()
                ,billingPlan.getPaymentMethod()
        );
    }
}
