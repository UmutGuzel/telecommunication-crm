package com.gygy.contractservice.service.impl;

import com.gygy.contractservice.client.PlanClient;
import com.gygy.contractservice.dto.billingPlan.*;
import com.gygy.contractservice.entity.BillingPlan;
import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.mapper.BillingPlanMapper;
import com.gygy.contractservice.repository.BillingPlanRepository;
import com.gygy.contractservice.rules.BillingPlanBusinessRules;
import com.gygy.contractservice.service.BillingPlanService;
import com.gygy.contractservice.core.exception.type.BusinessException;
import com.gygy.contractservice.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gygy.contractservice.constant.GeneralConstant.FETCHING_ALL_BILLING_PLANS;

@Service
public class BillingPlanServiceImpl implements BillingPlanService {
    private final BillingPlanRepository billingPlanRepository;
    private final ContractService contractService;
    private final BillingPlanBusinessRules billingPlanBusinessRules;
    private final BillingPlanMapper billingPlanMapper;
    private static final Logger logger = LoggerFactory.getLogger(BillingPlanServiceImpl.class);
    private final PlanClient planClient;


    public BillingPlanServiceImpl(
            BillingPlanRepository billingPlanRepository, ContractService contractService,
            BillingPlanBusinessRules billingPlanBusinessRules, BillingPlanMapper billingPlanMapper, PlanClient planClient) {
        this.billingPlanRepository = billingPlanRepository;
        this.contractService = contractService;
        this.billingPlanBusinessRules = billingPlanBusinessRules;
        this.billingPlanMapper = billingPlanMapper;
        this.planClient = planClient;
    }
    @Override
    public List<BillingPlan> findAll(List<UUID> billingPlanId) {
        logger.debug(FETCHING_ALL_BILLING_PLANS);
        List<BillingPlan> billingPlans = billingPlanRepository.findAllById(billingPlanId);
        if (billingPlans.isEmpty()) {
            logger.warn("No Billing Plans found with ID: {}", billingPlanId);
        } else {
            logger.info("Billing Plans found with ID: {}", billingPlanId);
        }

        return billingPlans;
    }


    @Override
    public BillingPlan findById(UUID id) {
        logger.debug("Searching for billing plan with ID: {}", id);
        BillingPlan billingPlan = billingPlanRepository.findById(id).orElseThrow(
                () -> new BusinessException("Billing plan not found with id: " + id));
        if (billingPlan != null) {
            logger.info("Billing Plan found with ID: {}", id);
        } else {
            logger.warn("Billing Plan not found with ID: {}", id);
        }
        return billingPlan;
    }

    @Override
    public void add(CreateBillingPlanDto createBillingPlanDto) {

       // billingPlanBusinessRules.checkIfBillingPlanNameExists(createBillingPlanDto.getName());
        
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
            createBillingPlanDto.getBaseAmount() ,
            createBillingPlanDto.getTaxRate()
        );
        
        Contract contract = contractService.findById(createBillingPlanDto.getContract()).orElseThrow(()-> new RuntimeException("Contract Not Found"));

        try {
            BillingPlan billingPlan=billingPlanMapper.createBillingPlanFromCreateBillingPlanDto(createBillingPlanDto);
            billingPlan.setContract(contract);
            PlanDto response=planClient.getCustomerByPhoneNumber(billingPlan.getPlanId());
            billingPlan.setName(response.getName());
            billingPlan.setDescription(response.getDescription());
            billingPlanRepository.save(billingPlan);
            logger.info("Successfully created billing plan with ID: {}", billingPlan.getId());
        } catch (Exception e) {
            logger.error("Error creating billing plan: {}", e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public List<BillingPlanListiningDto> getAll() {

        logger.debug(FETCHING_ALL_BILLING_PLANS);
        List<BillingPlan>  billingPlans = billingPlanRepository.findAll();
        List<BillingPlanListiningDto> billingPlanListiningDtos = billingPlans.stream()
                .map(billingPlanMapper::toBillingPlanListiningDto )
                .collect(Collectors.toList());
        logger.info("Found {} billing plan", billingPlanListiningDtos.size());
        return billingPlanListiningDtos;
    }

    @Override
    public BillingPlan update(UpdateBillingPlanDto updateBillingPlanDto) {

        billingPlanBusinessRules.checkIfBillingPlanNameExistsForUpdate(
            updateBillingPlanDto.getId(),
            updateBillingPlanDto.getName()
        );
        
        billingPlanBusinessRules.checkIfCycleTypeAndBillingDayAreConsistent(
            updateBillingPlanDto.getCycleType().toString(),
            updateBillingPlanDto.getBillingDay()
        );
        billingPlanBusinessRules.checkIfPaymentMethodAndDueDaysAreConsistent(
            updateBillingPlanDto.getPaymentMethod().toString(),
            updateBillingPlanDto.getPaymentDueDays()
        );
        billingPlanBusinessRules.checkIfBaseAmountAndTaxRateAreValid(
            updateBillingPlanDto.getBaseAmount(),
            updateBillingPlanDto.getTaxRate()
        );

        Contract contract = contractService.findById(updateBillingPlanDto.getContractId()).orElseThrow(()->new RuntimeException("Contract Not Found"));



        logger.info("Updating billing plan with ID: {}", updateBillingPlanDto.getId());
        try {
            BillingPlan billingPlan1 =billingPlanMapper.updateBillingPlanFromUpdateBillingPlanDto(updateBillingPlanDto);
            billingPlan1.setContract(contract);
            billingPlan1.setId(updateBillingPlanDto.getId());
            BillingPlan updatedBillingPlan=billingPlanRepository.save(billingPlan1);
            logger.info("Successfully updated billing plan with ID: {}", updatedBillingPlan.getId()); //sabite bak
            return updatedBillingPlan;
        } catch (Exception e) {
            logger.error("Error updating billing plan: {}", e.getMessage(), e);
            throw e;
        }
    }
    @Override
    public void delete(DeleteBillingPlanDto deleteBillingPlanDto) {
        logger.info("Attempting to delete billing plan with ID: {}", deleteBillingPlanDto.getId());
        try {
            BillingPlan billingPlan = billingPlanRepository.findById(deleteBillingPlanDto.getId())
                    .orElseThrow(() -> {
                        logger.error("Billing Plan not found for deletion with ID: {}", deleteBillingPlanDto.getId());
                        return new BusinessException("Billing Plan not found");
                    });
            billingPlanRepository.delete(billingPlan);
            logger.info("Successfully deleted billing plan with ID: {}", deleteBillingPlanDto.getId());
        } catch (Exception e) {
            logger.error("Error deleting billing plan: {}", e.getMessage(), e);
            throw e;
        }
    }

}
