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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service

public class CommitmentServiceImpl implements CommitmentService {
    private final CommitmentRepository commitmentRepository;
    private final ContractDetailService contractDetailService;
    private final CommitmentBusinessRules commitmentBusinessRules;
    private final CommitmentMapper commitmentMapper;

    public CommitmentServiceImpl(CommitmentRepository commitmentRepository,
                                 @Lazy ContractDetailService contractDetailService, CommitmentBusinessRules commitmentBusinessRules, CommitmentMapper commitmentMapper) {
        this.commitmentRepository = commitmentRepository;
        this.contractDetailService = contractDetailService;
        this.commitmentBusinessRules = commitmentBusinessRules;
        this.commitmentMapper = commitmentMapper;
    }

    @Override
    public Commitment findById(UUID id) {
        return commitmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Commitment not found with id: " + id));
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

        Commitment commitment=commitmentMapper.createCommitmentFromCreateCommitmentDto(createCommitmentDto);
        commitment.setContractDetail(contractDetail);
        commitmentRepository.save(commitment);
    }

    @Override
    public List<CommitmentListiningDto> getAll() {
        List<CommitmentListiningDto> commitmentListiningDtos = commitmentRepository
                .findAll()
                .stream()
                .map(commitment -> new CommitmentListiningDto(commitment.getStatus(), commitment.getStartDate(),
                        commitment.getEndDate(), commitment.getDurationMonths(), commitment.getContractDetail(),
                        commitment.getEarlyTerminationFee()))
                .toList();
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

        Commitment commitment=commitmentMapper.updateCommitmentFromUpdateCommitmentDto(updateCommitmentDto);
          commitment.setContractDetail(contractDetail);
        commitment.setId(updateCommitmentDto.getId());
        return commitmentRepository.save(commitment);



    }

    @Override
    public void delete(DeleteCommitmentDto deleteCommitmentDto) {
        Commitment commitment = commitmentRepository.findById(deleteCommitmentDto.getId()).orElseThrow(
                () -> new BusinessException("Commitment not found with id: " + deleteCommitmentDto.getId()));
        commitmentRepository.delete(commitment);
    }
}
