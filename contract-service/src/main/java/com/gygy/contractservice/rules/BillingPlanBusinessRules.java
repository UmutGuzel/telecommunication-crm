package com.gygy.contractservice.rules;

import com.gygy.contractservice.core.exception.type.BusinessException;
import com.gygy.contractservice.repository.BillingPlanRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class BillingPlanBusinessRules {
    
    private final BillingPlanRepository billingPlanRepository;
    
    public BillingPlanBusinessRules(BillingPlanRepository billingPlanRepository) {
        this.billingPlanRepository = billingPlanRepository;
    }

    public void checkIfBillingPlanNameExists(String name) {
        if (billingPlanRepository.existsByName(name)) {
            throw new BusinessException("A billing plan with name '" + name + "' already exists");
        }
    }
    

    public void checkIfBillingPlanNameExistsForUpdate(UUID id, String name) {
        if (billingPlanRepository.existsByNameAndIdNot(name, id)) {
            throw new BusinessException("A billing plan with name '" + name + "' already exists");
        }
    }



    



}
