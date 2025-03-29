package com.gygy.contractservice.service.impl;

import com.gygy.contractservice.core.exception.type.BusinessException;
import com.gygy.contractservice.dto.commitment.CommitmentListiningDto;
import com.gygy.contractservice.dto.commitment.CreateCommitmentDto;
import com.gygy.contractservice.dto.commitment.DeleteCommitmentDto;
import com.gygy.contractservice.dto.commitment.UpdateCommitmentDto;
import com.gygy.contractservice.entity.Commitment;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.mapper.CommitmentMapper;
import com.gygy.contractservice.repository.CommitmentRepository;
import com.gygy.contractservice.rules.CommitmentBusinessRules;
import com.gygy.contractservice.service.CommitmentService;
import com.gygy.contractservice.service.ContractDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import static com.gygy.contractservice.constant.GeneralConstant.FETCHING_ALL_COMMITMENTS;

@Service

public class CommitmentServiceImpl implements CommitmentService {
    private final CommitmentRepository commitmentRepository;
    private final ContractDetailService contractDetailService;
    private final CommitmentBusinessRules commitmentBusinessRules;
    private final CommitmentMapper commitmentMapper;
    private static final Logger logger = LoggerFactory.getLogger(CommitmentServiceImpl.class);


    public CommitmentServiceImpl(CommitmentRepository commitmentRepository,
                                 @Lazy ContractDetailService contractDetailService, CommitmentBusinessRules commitmentBusinessRules, CommitmentMapper commitmentMapper) {
        this.commitmentRepository = commitmentRepository;
        this.contractDetailService = contractDetailService;
        this.commitmentBusinessRules = commitmentBusinessRules;
        this.commitmentMapper = commitmentMapper;
    }

    @Override
    public Commitment findById(UUID id) {
        logger.debug("Searching for commitment with ID: {}", id);
        Commitment commitment = commitmentRepository.findById(id).orElseThrow(
                () -> new BusinessException("Commitment not found with id: " + id));
        if (commitment != null) {
            logger.info("Commitment found with ID: {}", id);
        } else {
            logger.warn("Commitment not found with ID: {}", id);
        }
        return commitment;
    }

    @Override
    public void add(CreateCommitmentDto createCommitmentDto) {
        commitmentBusinessRules.checkIfCommitmentNameExists(createCommitmentDto.getName());
        if (createCommitmentDto.getCycleType() != null && createCommitmentDto.getBillingDay() != null) {
            commitmentBusinessRules.checkIfCycleTypeAndBillingDayAreConsistent(
                    createCommitmentDto.getCycleType().toString(),
                    createCommitmentDto.getBillingDay());
        }
        if (createCommitmentDto.getStartDate() != null && createCommitmentDto.getEndDate() != null) {
            commitmentBusinessRules.checkIfCommitmentDatesAreValid(
                    createCommitmentDto.getStartDate(),
                    createCommitmentDto.getEndDate());
        }
        ContractDetail contractDetail = contractDetailService.findById(createCommitmentDto.getContractDetailId());

        // Check if the customer can make a new commitment
        commitmentBusinessRules.checkIfCustomerCanMakeNewCommitment(contractDetail.getCustomerId());

        logger.info("Creating new commitment with name: {}", createCommitmentDto.getName());
        try {
            Commitment commitment=commitmentMapper.createCommitmentFromCreateCommitmentDto(createCommitmentDto);
            commitment.setContractDetail(contractDetail);
            commitmentRepository.save(commitment);
            logger.info("Successfully created commitment with ID: {}", commitment.getId());
        } catch (Exception e) {
            logger.error("Error creating commitment: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<CommitmentListiningDto> getAll() {

        logger.debug(FETCHING_ALL_COMMITMENTS);
        List<Commitment> commitments = commitmentRepository.findAll();
        List<CommitmentListiningDto> commitmentListiningDtos = commitments.stream()
                .map(commitmentMapper::toCommitmentListiningDto)
                .collect(Collectors.toList());
        logger.info("Found {} commitment", commitmentListiningDtos.size());
        return commitmentListiningDtos;
    }

    @Override
    public Commitment update(UpdateCommitmentDto updateCommitmentDto) {
         ContractDetail contractDetail
         =contractDetailService.findById(updateCommitmentDto.getContractDetailId());

        commitmentBusinessRules.checkIfCommitmentNameExistsForUpdate(
                updateCommitmentDto.getId(),
                updateCommitmentDto.getName());
        if (updateCommitmentDto.getCycleType() != null && updateCommitmentDto.getBillingDay() != null) {
            commitmentBusinessRules.checkIfCycleTypeAndBillingDayAreConsistent(
                    updateCommitmentDto.getCycleType().toString(),
                    updateCommitmentDto.getBillingDay());
        }
        if (updateCommitmentDto.getStartDate() != null && updateCommitmentDto.getEndDate() != null) {
            commitmentBusinessRules.checkIfCommitmentDatesAreValid(
                    updateCommitmentDto.getStartDate(),
                    updateCommitmentDto.getEndDate());
        }


        logger.info("Updating contract with ID: {}", updateCommitmentDto.getId());
        try {
            Commitment commitment = commitmentMapper.updateCommitmentFromUpdateCommitmentDto(updateCommitmentDto);
            commitment.setId(updateCommitmentDto.getId());
            commitment.setContractDetail(contractDetail);
            Commitment updatedCommitment = commitmentRepository.save(commitment);
            logger.info("Successfully updated contract with ID: {}", updatedCommitment.getId());
            return updatedCommitment;
        } catch (Exception e) {
            logger.error("Error updating commitment: {}", e.getMessage(), e);
            throw e;
        }



    }

    @Override
    public void delete(DeleteCommitmentDto deleteCommitmentDto) {

      logger.info("Attempting to delete contract with ID: {}", deleteCommitmentDto.getId());
        try {
        Commitment commitment = commitmentRepository.findById(deleteCommitmentDto.getId())
                .orElseThrow(() -> {
                    logger.error("Commitment not found for deletion with ID: {}", deleteCommitmentDto.getId());
                    return new RuntimeException("Commitment not found");
                });
        commitmentRepository.delete(commitment);
        logger.info("Successfully deleted commitment with ID: {}", deleteCommitmentDto.getId());
    } catch (Exception e) {
        logger.error("Error deleting commitment: {}", e.getMessage(), e);
        throw e;
    }
}
}
