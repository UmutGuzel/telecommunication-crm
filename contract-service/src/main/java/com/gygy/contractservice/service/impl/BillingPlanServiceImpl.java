package com.gygy.contractservice.service.impl;

import com.gygy.contractservice.dto.billingPlan.BillingPlanListiningDto;
import com.gygy.contractservice.dto.billingPlan.CreateBillingPlanDto;
import com.gygy.contractservice.dto.billingPlan.DeleteBillingPlanDto;
import com.gygy.contractservice.dto.billingPlan.UpdateBillingPlanDto;
import com.gygy.contractservice.entity.BillingPlan;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.entity.Discount;
import com.gygy.contractservice.repository.BillingPlanRepository;
import com.gygy.contractservice.service.BillingPlanService;
import com.gygy.contractservice.service.ContractDetailService;
import com.gygy.contractservice.service.DiscountService;
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

    public BillingPlanServiceImpl(BillingPlanRepository billingPlanRepository, ContractDetailService contractDetailService,@Lazy DiscountService discountService) {
        this.billingPlanRepository = billingPlanRepository;
        this.contractDetailService = contractDetailService;
        this.discountService = discountService;
    }

    @Override
    public List<BillingPlan> findAll(List<UUID> billingPlanId) {
        return billingPlanRepository.findAllById(billingPlanId);
    }

    @Override
    public BillingPlan findById(UUID id) {
       return billingPlanRepository.findById(id).orElseThrow(()-> new RuntimeException("Billing plan not found"));
    }

    @Override
    public void add(CreateBillingPlanDto createBillingPlanDto) {
        List<Discount> discounts=discountService.findAllById(createBillingPlanDto.getDiscountIds());
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
        List<BillingPlanListiningDto> billingPlanListiningDtos =
                billingPlanRepository
                        .findAll()
                        .stream()
                        .map((billingPlan)-> new BillingPlanListiningDto(billingPlan.getName()
                                        ,billingPlan.getDescription()
                                        ,billingPlan.getBillingDay()
                                        ,billingPlan.getCycleType()
                                        ,billingPlan.getBaseAmount()
                                        ,billingPlan.getContractDetail()
                                        ,billingPlan.getPaymentDueDays()
                                        ,billingPlan.getStatus()
                                        ,billingPlan.getTaxRate())).toList();
        return billingPlanListiningDtos;
    }

    @Override
    public BillingPlan update(UpdateBillingPlanDto updateBillingPlanDto) {
        BillingPlan billingPlan = billingPlanRepository.findById(updateBillingPlanDto.getId()).orElseThrow(()-> new RuntimeException("Billing Plan not found"));
        ContractDetail contractDetail= contractDetailService.findById(updateBillingPlanDto.getContractDetailId());

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
        BillingPlan billingPlan=billingPlanRepository.findById(deleteBillingPlanDto.getId()).orElseThrow(()-> new RuntimeException("Billing Plan not found"));
        billingPlanRepository.delete(billingPlan);

    }
}
