package com.gygy.contractservice.service.impl;

import com.gygy.contractservice.dto.discount.CreateDiscountDto;
import com.gygy.contractservice.dto.discount.DeleteDiscountDto;
import com.gygy.contractservice.dto.discount.DiscountListiningDto;
import com.gygy.contractservice.dto.discount.UpdateDiscountDto;
import com.gygy.contractservice.entity.BillingPlan;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.entity.Discount;
import com.gygy.contractservice.model.enums.DiscountType;
import com.gygy.contractservice.model.enums.Status;
import com.gygy.contractservice.repository.DiscountRepository;
import com.gygy.contractservice.service.BillingPlanService;
import com.gygy.contractservice.service.ContractDetailService;
import com.gygy.contractservice.service.DiscountService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final BillingPlanService billingPlanService;
    private final ContractDetailService contractDetailService;

    public DiscountServiceImpl(DiscountRepository discountRepository, BillingPlanService billingPlanService, ContractDetailService contractDetailService) {
        this.discountRepository = discountRepository;
        this.billingPlanService = billingPlanService;
        this.contractDetailService = contractDetailService;
    }

    @Override
    public List<Discount> findAllById(List<UUID> ids) {
        return discountRepository.findAllById(ids);
    }

    @Override
    public Discount findById(UUID id) {
        return discountRepository.findById(id).orElse(null);
    }

    @Override
    public void add(CreateDiscountDto createDiscountDto) {
        List<BillingPlan> billingPlan = billingPlanService.findAll(createDiscountDto.getBillingPlanId());
        ContractDetail contractDetail = contractDetailService.findById(createDiscountDto.getContractDetailId());
        Discount discount = new Discount();
        discount.setDiscountType(createDiscountDto.getDiscountType());
        discount.setAmount(createDiscountDto.getAmount());
        discount.setBillingPlans(billingPlan);
        discount.setPercentage(createDiscountDto.getPercentage());
        discount.setStartDate(createDiscountDto.getStartDate());
        discount.setEndDate(createDiscountDto.getEndDate());
        discount.setContractDetail(contractDetail);
        discountRepository.save(discount);
    }

    @Override
    public List<DiscountListiningDto> getAll() {
        List<DiscountListiningDto> discountListiningDtos = discountRepository
                .findAll()
                .stream()
                .map(discount -> new DiscountListiningDto(discount.getDiscountType(), discount.getAmount(), discount.getPercentage(), discount.getContractDetail(), discount.getEndDate(), discount.getStartDate(), discount.getBillingPlans(), discount.getCreatedAt(), discount.getUpdatedAt())).toList();
        return discountListiningDtos;
    }

    @Override
    public Discount update(UpdateDiscountDto updateDiscountDto) {
        ContractDetail contractDetail = contractDetailService.findById(updateDiscountDto.getContractDetailId());
        Discount discount = discountRepository.findById(updateDiscountDto.getId()).orElseThrow(() -> new RuntimeException("Discount not found"));
        discount.setDiscountType(updateDiscountDto.getDiscountType());
        discount.setAmount(updateDiscountDto.getAmount());
        discount.setPercentage(updateDiscountDto.getPercentage());
        discount.setStartDate(updateDiscountDto.getStartDate());
        discount.setEndDate(updateDiscountDto.getEndDate());
        discount.setAmount(updateDiscountDto.getAmount());
        discount.setContractDetail(contractDetail);
        return discountRepository.save(discount);
    }

    @Override
    public void delete(DeleteDiscountDto deleteDiscountDto) {
        Discount discount = discountRepository.findById(deleteDiscountDto.getId()).orElseThrow(() -> new RuntimeException("Discount not found"));
        discountRepository.delete(discount);

    }

    @Override
    public List<DiscountListiningDto> getActiveDiscounts() {
        return discountRepository.findAll().stream()
                .filter(discounts -> "ACTIVE".equals(discounts.getStatus().toString()))
                .map(discount -> new DiscountListiningDto(discount.getDiscountType()
                        , discount.getAmount()
                        , discount.getPercentage()
                        , discount.getContractDetail()
                        , discount.getEndDate(),
                        discount.getStartDate()
                        , discount.getBillingPlans()
                        , discount.getCreatedAt()
                        , discount.getUpdatedAt()))
                .toList();
    }

    @Override
    public List<DiscountListiningDto> getActiveDiscountsByCustomerId(UUID customerId) {
        return discountRepository.findAll().stream()
                .filter(discount -> "ACTIVE".equals(discount.getStatus().toString()) && discount.getCustomerId().equals(customerId))
                .map(discount -> new DiscountListiningDto(discount.getDiscountType()
                        , discount.getAmount()
                        , discount.getPercentage()
                        , discount.getContractDetail()
                        , discount.getEndDate(),
                        discount.getStartDate()
                        , discount.getBillingPlans()
                        , discount.getCreatedAt()
                        , discount.getUpdatedAt()))
                .toList();
    }

    @Override
    public List<DiscountListiningDto> getDiscountsByContractId(UUID contractId) {
        return discountRepository.findAll().stream()
                .filter(discount -> discount.getContractDetail().getId().equals(contractId))
                .map(discount -> new DiscountListiningDto(discount.getDiscountType()
                        , discount.getAmount()
                        , discount.getPercentage()
                        , discount.getContractDetail()
                        , discount.getEndDate(),
                        discount.getStartDate()
                        , discount.getBillingPlans()
                        , discount.getCreatedAt()
                        , discount.getUpdatedAt()))
                .toList();
    }


    @Override
    public Discount applyDiscountForAnnualPackage(CreateDiscountDto createDiscountDto) {
        if ("ANNUAL".equals(createDiscountDto.getBillingCycleType().toString())) {
            double discountedPrice = calculateDiscountedPrice(createDiscountDto.getAmount(), 5.0);

            Discount discount = new Discount();
            discount.setDiscountType(DiscountType.YEARLY_SUBSCRIPTION);
            discount.setPercentage(5.0);
            discount.setAmount(discountedPrice);
            discount.setStartDate(LocalDate.now());
            discount.setEndDate(createDiscountDto.getEndDate());
            discount.setCreatedAt(LocalDate.now());
            discount.setUpdatedAt(LocalDate.now());
            discount.setStatus(Status.ACTIVE);
            discount.setCustomerId(createDiscountDto.getCustomerId());

            return discountRepository.save(discount);
        }
        throw new IllegalArgumentException("Invalid billing cycle type: " + createDiscountDto.getBillingCycleType());
    }

    private double calculateDiscountedPrice(double originalPrice, double discountPercentage) {
        double discount = originalPrice * (discountPercentage / 100);
        return originalPrice - discount;
    }
}
