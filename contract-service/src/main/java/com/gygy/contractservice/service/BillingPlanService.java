package com.gygy.contractservice.service;

import com.gygy.contractservice.dto.billingPlan.BillingPlanListiningDto;
import com.gygy.contractservice.dto.billingPlan.CreateBillingPlanDto;
import com.gygy.contractservice.dto.billingPlan.DeleteBillingPlanDto;
import com.gygy.contractservice.dto.billingPlan.UpdateBillingPlanDto;
import com.gygy.contractservice.entity.BillingPlan;
import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.dto.contract.CreateContractDto;
import java.util.List;
import java.util.UUID;

public interface BillingPlanService {
    List<BillingPlan> findAll(List<UUID> billingPlanId);

    BillingPlan findById(UUID id);

    void add(Contract contract, CreateContractDto createContractDto);

    List<BillingPlanListiningDto> getAll();

    BillingPlan update(UpdateBillingPlanDto billingPlanDto);

    void delete(DeleteBillingPlanDto deleteBillingPlanDto);
}
