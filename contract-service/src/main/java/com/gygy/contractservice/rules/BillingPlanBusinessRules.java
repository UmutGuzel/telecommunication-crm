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

    public void checkIfCycleTypeAndBillingDayAreConsistent(String cycleType, int billingDay) {

        // Özel iş kuralı: 3 ayda bir faturalama için, fatura günü ayın ilk 10 günü içinde olmalı
        if (cycleType.equals("QUARTERLY") && billingDay > 10) {
            throw new BusinessException("For quarterly billing cycle, billing day must be between 1 and 10");
        }
        
        // Özel iş kuralı: Yıllık faturalama için, fatura günü ayın ilk 5 günü içinde olmalı
        if (cycleType.equals("ANNUAL") && billingDay > 5) {
            throw new BusinessException("For annual billing cycle, billing day must be between 1 and 5");
        }
    }

    public void checkIfPaymentMethodAndDueDaysAreConsistent(String paymentMethod, int paymentDueDays) {
        // Özel iş kuralı: Kredi kartı ödemelerinde vade 0 gün olmalı (anında tahsilat)
        if (paymentMethod.equals("CREDIT_CARD") && paymentDueDays > 0) {
            throw new BusinessException("For credit card payments, payment due days must be 0");
        }
        
        // Özel iş kuralı: Nakit ödemelerde maksimum 7 gün vade olabilir
        if (paymentMethod.equals("CASH") && paymentDueDays > 7) {
            throw new BusinessException("For cash payments, payment due days cannot exceed 7 days");
        }
    }
    
    public void checkIfBaseAmountAndTaxRateAreValid(Integer baseAmount, Double taxRate) {

        // Özel iş kuralı: Belirli bir tutarın altındaki faturalarda vergi istisnası olabilir
        if (baseAmount < 50 && taxRate > 0) {
            throw new BusinessException("Tax exemption applies for amounts below 50.0");
        }

        // Özel iş kuralı: Çok yüksek tutarlı faturalarda özel vergi uygulaması
        if (baseAmount > 10000 && taxRate != 18.0) {
            throw new BusinessException("Special tax rate of 18% applies for amounts above 10000.0");
        }
    }

}
