package com.gygy.contractservice.service.impl;

import com.gygy.contractservice.dto.billingPlan.BillingPlanListiningDto;
import com.gygy.contractservice.dto.billingPlan.CreateBillingPlanDto;
import com.gygy.contractservice.dto.billingPlan.DeleteBillingPlanDto;
import com.gygy.contractservice.dto.billingPlan.UpdateBillingPlanDto;
import com.gygy.contractservice.entity.BillingPlan;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.entity.Discount;
import com.gygy.contractservice.repository.BillingPlanRepository;
import com.gygy.contractservice.rules.BillingPlanBusinessRules;
import com.gygy.contractservice.service.BillingPlanService;
import com.gygy.contractservice.service.ContractDetailService;
import com.gygy.contractservice.service.DiscountService;
import com.gygy.contractservice.core.exception.type.BusinessException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BillingPlanServiceImpl implements BillingPlanService {
    private final BillingPlanRepository billingPlanRepository;
    private final ContractDetailService contractDetailService;
    private final DiscountService discountService;
    private final BillingPlanBusinessRules billingPlanBusinessRules;

    public BillingPlanServiceImpl(
            BillingPlanRepository billingPlanRepository, 
            ContractDetailService contractDetailService, 
            @Lazy DiscountService discountService, 
            BillingPlanBusinessRules billingPlanBusinessRules) {
        this.billingPlanRepository = billingPlanRepository;
        this.contractDetailService = contractDetailService;
        this.discountService = discountService;
        this.billingPlanBusinessRules = billingPlanBusinessRules;
    }

    @Override
    public List<BillingPlan> findAll(List<UUID> billingPlanId) {
        return billingPlanRepository.findAllById(billingPlanId);
    }

    @Override
    public BillingPlan findById(UUID id) {
       return billingPlanRepository.findById(id)
               .orElseThrow(() -> new BusinessException("Billing plan not found with id: " + id));
    }

    @Override
    public void add(CreateBillingPlanDto createBillingPlanDto) {

        billingPlanBusinessRules.checkIfBillingPlanNameExists(createBillingPlanDto.getName());
        
        // 2. Döngü tipi ve faturalama günü tutarlılığı
        billingPlanBusinessRules.checkIfCycleTypeAndBillingDayAreConsistent(
            createBillingPlanDto.getCycleType().toString(),
            createBillingPlanDto.getBillingDay()
        );
        
        // 3. Ödeme yöntemi ve vade tutarlılığı
        billingPlanBusinessRules.checkIfPaymentMethodAndDueDaysAreConsistent(
            createBillingPlanDto.getPaymentMethod().toString(),
            createBillingPlanDto.getPaymentDueDays()
        );
        
        // 4. Temel ücret ve vergi oranı kontrolü
        billingPlanBusinessRules.checkIfBaseAmountAndTaxRateAreValid(
            createBillingPlanDto.getBaseAmount(),
            createBillingPlanDto.getTaxRate()
        );
        
        List<Discount> discounts = discountService.findAllById(createBillingPlanDto.getDiscountIds());
        ContractDetail contractDetail = contractDetailService.findById(createBillingPlanDto.getContractDetailId());
        
        BillingPlan billingPlan = new BillingPlan();
        billingPlan.setName(createBillingPlanDto.getName());
        billingPlan.setDescription(createBillingPlanDto.getDescription());
        billingPlan.setBaseAmount(createBillingPlanDto.getBaseAmount());
        billingPlan.setContractDetail(contractDetail);
        billingPlan.setCycleType(createBillingPlanDto.getCycleType());
        billingPlan.setDiscounts(discounts);
        billingPlan.setBillingDay(createBillingPlanDto.getBillingDay());
        billingPlan.setPaymentDueDays(createBillingPlanDto.getPaymentDueDays());
        billingPlan.setPaymentMethod(createBillingPlanDto.getPaymentMethod());
        billingPlan.setStatus(createBillingPlanDto.getStatus());
        billingPlan.setTaxRate(createBillingPlanDto.getTaxRate());
        billingPlanRepository.save(billingPlan);
    }

    @Override
    public List<BillingPlanListiningDto> getAll() {
        return billingPlanRepository.findAll().stream().map(billingPlan-> new BillingPlanListiningDto(billingPlan.getName()
                                        ,billingPlan.getDescription()
                                        ,billingPlan.getBillingDay()
                                        ,billingPlan.getCycleType()
                                        ,billingPlan.getBaseAmount()
                                        ,billingPlan.getContractDetail()
                                        ,billingPlan.getPaymentDueDays()
                                        ,billingPlan.getStatus()
                                        ,billingPlan.getTaxRate())).toList();
    }

    @Override
    public BillingPlan update(UpdateBillingPlanDto updateBillingPlanDto) {
        BillingPlan billingPlan = billingPlanRepository.findById(updateBillingPlanDto.getId())
                .orElseThrow(() -> new BusinessException("Billing Plan not found with id: " + updateBillingPlanDto.getId()));
        
        // İş kurallarını tek tek kontrol et
        // 1. İsim benzersizliği kontrolü (güncelleme için)
        billingPlanBusinessRules.checkIfBillingPlanNameExistsForUpdate(
            updateBillingPlanDto.getId(),
            updateBillingPlanDto.getName()
        );
        
        // 2. Döngü tipi ve faturalama günü tutarlılığı
        billingPlanBusinessRules.checkIfCycleTypeAndBillingDayAreConsistent(
            updateBillingPlanDto.getCycleType().toString(),
            updateBillingPlanDto.getBillingDay()
        );
        
        // 3. Ödeme yöntemi ve vade tutarlılığı
        billingPlanBusinessRules.checkIfPaymentMethodAndDueDaysAreConsistent(
            updateBillingPlanDto.getPaymentMethod().toString(),
            updateBillingPlanDto.getPaymentDueDays()
        );
        
        // 4. Temel ücret ve vergi oranı kontrolü
        billingPlanBusinessRules.checkIfBaseAmountAndTaxRateAreValid(
            updateBillingPlanDto.getBaseAmount(),
            updateBillingPlanDto.getTaxRate()
        );
        
        ContractDetail contractDetail = contractDetailService.findById(updateBillingPlanDto.getContractDetailId());

        billingPlan.setDescription(updateBillingPlanDto.getDescription());
        billingPlan.setBaseAmount(updateBillingPlanDto.getBaseAmount());
        billingPlan.setCycleType(updateBillingPlanDto.getCycleType());
        billingPlan.setBillingDay(updateBillingPlanDto.getBillingDay());
        billingPlan.setDiscounts(updateBillingPlanDto.getDiscount());
        billingPlan.setPaymentDueDays(updateBillingPlanDto.getPaymentDueDays());
        billingPlan.setStatus(updateBillingPlanDto.getStatus());
        billingPlan.setTaxRate(updateBillingPlanDto.getTaxRate());
        billingPlan.setContractDetail(contractDetail);
        billingPlan.setPaymentMethod(updateBillingPlanDto.getPaymentMethod());
        billingPlan.setUpdatedAt(LocalDateTime.now());
        return billingPlanRepository.save(billingPlan);
    }

    @Override
    public void delete(DeleteBillingPlanDto deleteBillingPlanDto) {
        BillingPlan billingPlan = billingPlanRepository.findById(deleteBillingPlanDto.getId())
                .orElseThrow(() -> new BusinessException("Billing Plan not found with id: " + deleteBillingPlanDto.getId()));
        billingPlanRepository.delete(billingPlan);
    }
}
