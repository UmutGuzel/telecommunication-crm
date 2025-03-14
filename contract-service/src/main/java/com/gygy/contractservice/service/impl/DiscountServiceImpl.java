package com.gygy.contractservice.service.impl;

import com.gygy.contractservice.dto.discount.CreateDiscountDto;
import com.gygy.contractservice.dto.discount.DeleteDiscountDto;
import com.gygy.contractservice.dto.discount.DiscountListiningDto;
import com.gygy.contractservice.dto.discount.UpdateDiscountDto;
import com.gygy.contractservice.entity.BillingPlan;
import com.gygy.contractservice.entity.Commitment;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.entity.Discount;
import com.gygy.contractservice.repository.DiscountRepository;
import com.gygy.contractservice.service.BillingPlanService;
import com.gygy.contractservice.service.ContractDetailService;
import com.gygy.contractservice.service.DiscountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        List<BillingPlan> billingPlan=billingPlanService.findAll(createDiscountDto.getBillingPlanId());
        ContractDetail contractDetail=contractDetailService.findById(createDiscountDto.getContractDetailId());
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
        List<DiscountListiningDto> discountListiningDtos=discountRepository
                .findAll()
                .stream()
                .map((discount)-> new DiscountListiningDto(discount.getDiscountType(),discount.getAmount(),discount.getPercentage(),discount.getContractDetail(),discount.getEndDate(),discount.getStartDate(),discount.getBillingPlans(),discount.getCreatedAt(),discount.getUpdatedAt())).toList();
        return discountListiningDtos;
    }

    @Override
    public Discount update(UpdateDiscountDto updateDiscountDto) {
        ContractDetail contractDetail=contractDetailService.findById(updateDiscountDto.getContractDetailId());
        Discount discount=discountRepository.findById(updateDiscountDto.getId()).orElseThrow(()->new RuntimeException("Discount not found"));
        discount.setDiscountType(updateDiscountDto.getDiscountType());
        discount.setAmount(updateDiscountDto.getAmount());
        discount.setPercentage(updateDiscountDto.getPercentage());
        discount.setStartDate(updateDiscountDto.getStartDate());
        discount.setEndDate(updateDiscountDto.getEndDate());
        discount.setAmount(updateDiscountDto.getAmount());
        discount.setContractDetail(contractDetail);
        return  discountRepository.save(discount);
    }

    @Override
    public void delete(DeleteDiscountDto deleteDiscountDto) {
        Discount discount=discountRepository.findById(deleteDiscountDto.getId()).orElseThrow(()->new RuntimeException("Discount not found"));
        discountRepository.delete(discount);

    }
}
