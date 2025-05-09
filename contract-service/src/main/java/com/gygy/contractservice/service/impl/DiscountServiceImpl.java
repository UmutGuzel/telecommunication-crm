package com.gygy.contractservice.service.impl;

import com.gygy.contractservice.core.exception.type.BusinessException;
import com.gygy.contractservice.dto.discount.CreateDiscountDto;
import com.gygy.contractservice.dto.discount.DeleteDiscountDto;
import com.gygy.contractservice.dto.discount.DiscountListiningDto;
import com.gygy.contractservice.dto.discount.UpdateDiscountDto;
import com.gygy.contractservice.entity.BillingPlan;
import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.entity.Discount;
import com.gygy.contractservice.mapper.DiscountMapper;
import com.gygy.contractservice.model.enums.DiscountType;
import com.gygy.contractservice.model.enums.Status;
import com.gygy.contractservice.repository.DiscountRepository;
import com.gygy.contractservice.service.BillingPlanService;
import com.gygy.contractservice.service.ContractService;
import com.gygy.contractservice.service.DiscountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gygy.contractservice.constant.GeneralConstant.*;

@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final BillingPlanService billingPlanService;
    private final ContractService contractService;
    private static final Logger logger = LoggerFactory.getLogger(DiscountServiceImpl.class);
    private final DiscountMapper discountMapper;

    public DiscountServiceImpl(DiscountRepository discountRepository, BillingPlanService billingPlanService,
                                ContractService contractService, DiscountMapper discountMapper) {
        this.discountRepository = discountRepository;
        this.billingPlanService = billingPlanService;
        this.contractService = contractService;
        this.discountMapper = discountMapper;
    }

    @Override
    public List<Discount> findAllById(List<UUID> ids) {
        logger.debug(FETCHING_ALL_DISCOUNT);
        List<Discount> discounts = discountRepository.findAllById(ids);
        if (discounts.isEmpty()) {
            logger.warn("No Discount found with ID: {}", ids);
        } else {
            logger.info("Discount  found with ID: {}", ids);
        }
        return discounts;
    }

    @Override
    public Discount findById(UUID id) {
        logger.debug("Searching for billing plan with ID: {}", id);
        Discount discount = discountRepository.findById(id).orElseThrow(
                () -> new BusinessException("Discount not found with id: " + id));
        if (discount != null) {
            logger.info("Discount found with ID: {}", id);
        } else {
            logger.warn("Discount not found with ID: {}", id);
        }
        return discount;
    }

    @Override
    public void add(CreateDiscountDto createDiscountDto) {
        logger.info("Creating new discount with name: {}", createDiscountDto.getName());
        try {
            Discount discount = discountMapper.createDiscountFromCreateDiscountDto(createDiscountDto);
            
            // BillingPlan ve ContractDetail ilişkilerini null kontrolü ile ekleyelim
            if (createDiscountDto.getBillingPlanId() != null && !createDiscountDto.getBillingPlanId().isEmpty()) {
                List<BillingPlan> billingPlans = billingPlanService.findAll(createDiscountDto.getBillingPlanId());
                if (!billingPlans.isEmpty()) {
                    discount.setBillingPlan(billingPlans.get(0));
                }
            }
            
            discountRepository.save(discount);
            logger.info("Successfully created discount with ID: {}", discount.getId());
        } catch (Exception e) {
            logger.error("Error creating discount: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<DiscountListiningDto> getAll() {
        logger.debug(FETCHING_ALL_DISCOUNT);
        List<Discount>  discounts = discountRepository.findAll();
        List<DiscountListiningDto> discountListiningDtos = discounts.stream()
                .map(discountMapper::toDiscountListiningDto )
                .collect(Collectors.toList());
        logger.info("Found {}  discount", discountListiningDtos.size());
        return discountListiningDtos;
    }

    @Override
    public Discount update(UpdateDiscountDto updateDiscountDto) {
        logger.info("Updating discount  with ID: {}", updateDiscountDto.getId());
        try {
            Contract contract = contractService.findById(updateDiscountDto.getContractId()).orElseThrow(()->new RuntimeException("Contract Not Found."));
            Discount discount=discountMapper.updateDiscountFromUpdateDiscountDto(updateDiscountDto);
            discount.setId(updateDiscountDto.getId());
            discount.setContract(contract);
            Discount updatedDiscount=discountRepository.save(discount);
            logger.info("Successfully updated billing plan with ID: {}", updatedDiscount.getId());
            return updatedDiscount;
        } catch (Exception e) {
            logger.error("Error updating billing plan: {}", e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public void delete(DeleteDiscountDto deleteDiscountDto) {
        logger.info("Attempting to delete discount  with ID: {}", deleteDiscountDto.getId());
        try {
            Discount discount = discountRepository.findById(deleteDiscountDto.getId())
                    .orElseThrow(() -> {
                        logger.error("Discount  not found for deletion with ID: {}", deleteDiscountDto.getId());
                        return new BusinessException("Billing Plan not found");
                    });
            discountRepository.delete(discount);
            logger.info("Successfully deleted discount  with ID: {}", deleteDiscountDto.getId());
        } catch (Exception e) {
            logger.error("Error deleting  discount: {}", e.getMessage(), e);
            throw e;
        }
    }



    @Override
    public List<DiscountListiningDto> getActiveDiscounts() {
        return discountRepository.findAll().stream()
                .filter(discounts -> "ACTIVE".equals(discounts.getStatus().toString()))
                .map(discount -> new DiscountListiningDto(      discount.getDiscountType()
                        ,discount.getAmount()
                        ,discount.getPercentage()
                        ,discount.getContract()
                        ,discount.getEndDate()
                        ,discount.getStartDate()
                        ,discount.getCreatedAt(),
                        discount.getUpdatedAt()
                        ,discount.getStatus()
                        ,discount.getDescription()))
                .toList();
    }


    @Override
    public List<DiscountListiningDto> getDiscountsByContractId(UUID contractId) {
        logger.debug(FETCHING_ALL_DISCOUNT);
        List<Discount> discounts = discountRepository.findAll();
        List<DiscountListiningDto> discountListiningDtos = discounts.stream()
                .filter(discount -> discount.getContract().getId().equals(contractId))
                .map(discountMapper::toDiscountListiningDto)
                .collect(Collectors.toList());
        logger.info("Found {} active contracts", discounts.size());
        return discountListiningDtos;
    }

    @Override
    public Discount applyDiscountForAnnualPackage(CreateDiscountDto createDiscountDto) {
        if ("ANNUAL".equals(createDiscountDto.getBillingCycleType().toString())) {
            Discount discount = new Discount();
            discount.setDiscountType(DiscountType.YEARLY_SUBSCRIPTION);
            discount.setPercentage(5.0);
            discount.setStartDate(LocalDate.now());
            discount.setEndDate(createDiscountDto.getEndDate());
            discount.setCreatedAt(LocalDate.now());
            discount.setUpdatedAt(LocalDate.now());
            discount.setStatus(Status.ACTIVE);
            discount.setAmount(0.1);
            Discount savedDiscount = discountRepository.save(discount);

            return savedDiscount;
        }
        throw new IllegalArgumentException("Invalid billing cycle type: " + createDiscountDto.getBillingCycleType());
    }



}
