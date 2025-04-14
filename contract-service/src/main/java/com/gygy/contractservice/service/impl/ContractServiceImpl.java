package com.gygy.contractservice.service.impl;

import com.gygy.contractservice.dto.contract.ContractListiningDto;
import com.gygy.contractservice.dto.contract.CreateContractDto;
import com.gygy.contractservice.dto.contract.DeleteContractDto;
import com.gygy.contractservice.dto.contract.UpdateContractDto;
import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.mapper.ContractMapper;
import com.gygy.contractservice.repository.ContractRepository;
import com.gygy.contractservice.service.ContractService;
import com.gygy.contractservice.service.BillingPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gygy.contractservice.constant.GeneralConstant.*;

@Service
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final BillingPlanService billingPlanService;
    private static final Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

    public ContractServiceImpl(ContractRepository contractRepository, ContractMapper contractMapper,
            BillingPlanService billingPlanService) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.billingPlanService = billingPlanService;
    }

    @Override
    public Optional<Contract> findById(UUID id) {
        logger.debug("Searching for contract with ID: {}", id);
        Optional<Contract> contract = contractRepository.findById(id);
        if (contract.isPresent()) {
            logger.info("Contract found with ID: {}", id);
        } else {
            logger.warn("Contract not found with ID: {}", id);
        }
        return contract;
    }

    @Override
    public void add(CreateContractDto createContractDto) {
        logger.info("Creating new contract with customer email: {}", createContractDto.getCustomerEmail());
        try {
            Contract contract = contractMapper.createContractFromCreateContractDto(createContractDto);
            contractRepository.save(contract);
            billingPlanService.add(contract, createContractDto);
            // BillingPlan billingPlan = new BillingPlan();
            // billingPlan.setContract(contract);
            // PlanDto planDto = planClient.getPlanById(createContractDto.getPlanId());

            logger.info("Successfully created contract with ID: {}", contract.getId());

        } catch (Exception e) {
            logger.error("Error creating contract: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<ContractListiningDto> getAll() {
        logger.debug(FETCHING_ALL_CONTRACTS);
        List<Contract> contracts = contractRepository.findAll();
        List<ContractListiningDto> contractDtos = contracts.stream()
                .map(contractMapper::toContractListiningDto)
                .collect(Collectors.toList());
        logger.info("Found {} contracts", contractDtos.size());
        return contractDtos;

    }

    @Override
    public Contract update(UpdateContractDto updateContractDto) {
        logger.info("Updating contract with ID: {}", updateContractDto.getId());
        try {
            Contract contract = contractMapper.updateContractFromUpdateContractDto(updateContractDto);
            contract.setId(updateContractDto.getId());
            Contract updatedContract = contractRepository.save(contract);
            logger.info("Successfully updated contract with ID: {}", updatedContract.getId());
            return updatedContract;
        } catch (Exception e) {
            logger.error("Error updating contract: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void delete(DeleteContractDto deleteContractDto) {
        logger.info("Attempting to delete contract with ID: {}", deleteContractDto.getId());
        try {
            Contract contract = contractRepository.findById(deleteContractDto.getId())
                    .orElseThrow(() -> {
                        logger.error("Contract not found for deletion with ID: {}", deleteContractDto.getId());
                        return new RuntimeException("Contract not found");
                    });
            contractRepository.delete(contract);
            logger.info("Successfully deleted contract with ID: {}", deleteContractDto.getId());
        } catch (Exception e) {
            logger.error("Error deleting contract: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<ContractListiningDto> getActiveContracts() {
        logger.debug(FETCHING_ALL_CONTRACTS);
        List<Contract> contracts = contractRepository.findAll();
        List<ContractListiningDto> contractDtos = contracts.stream()
                .filter(contract -> STATUS_ACTIVE.equals(contract.getStatus().toString()))
                .map(contractMapper::toContractListiningDto)
                .collect(Collectors.toList());
        logger.info("Found {} active contracts", contractDtos.size());
        return contractDtos;
    }

    @Override
    public List<ContractListiningDto> getSuspendedContracts() {
        logger.debug(FETCHING_ALL_CONTRACTS);
        List<Contract> contracts = contractRepository.findAll();
        List<ContractListiningDto> suspendedContracts = contracts.stream()
                .filter(contract -> STATUS_SUSPENDED.equals(contract.getStatus().toString()))
                .map(contractMapper::toContractListiningDto)
                .collect(Collectors.toList());
        logger.info("Found {} suspended contracts", suspendedContracts.size());
        return suspendedContracts;

    }
}
