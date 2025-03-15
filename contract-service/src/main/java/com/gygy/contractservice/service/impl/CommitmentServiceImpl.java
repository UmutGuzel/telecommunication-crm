package com.gygy.contractservice.service.impl;

import com.gygy.contractservice.core.exception.type.BusinessException;
import com.gygy.contractservice.dto.commitment.CommitmentListiningDto;
import com.gygy.contractservice.dto.commitment.CreateCommitmentDto;
import com.gygy.contractservice.dto.commitment.DeleteCommitmentDto;
import com.gygy.contractservice.dto.commitment.UpdateCommitmentDto;
import com.gygy.contractservice.entity.Commitment;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.repository.CommitmentRepository;
import com.gygy.contractservice.rules.CommitmentBusinessRules;
import com.gygy.contractservice.service.CommitmentService;
import com.gygy.contractservice.service.ContractDetailService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service

public class CommitmentServiceImpl implements CommitmentService {
    private final CommitmentRepository commitmentRepository;
    private final ContractDetailService contractDetailService;
    private final CommitmentBusinessRules commitmentBusinessRules;

    public CommitmentServiceImpl(CommitmentRepository commitmentRepository, @Lazy ContractDetailService contractDetailService, CommitmentBusinessRules commitmentBusinessRules) {
        this.commitmentRepository = commitmentRepository;
        this.contractDetailService = contractDetailService;
        this.commitmentBusinessRules = commitmentBusinessRules;
    }

    @Override
    public Commitment findById(UUID id) {
        return commitmentRepository.findById(id).orElseThrow(() -> new BusinessException("Commitment not found with id: " + id));
    }

    @Override
    public void add(CreateCommitmentDto createCommitmentDto) {
        commitmentBusinessRules.checkIfCommitmentNameExists(createCommitmentDto.getName());
        if (createCommitmentDto.getCycleType() != null && createCommitmentDto.getBillingDay() != null) {
            commitmentBusinessRules.checkIfCycleTypeAndBillingDayAreConsistent(
                createCommitmentDto.getCycleType().toString(), 
                createCommitmentDto.getBillingDay()
            );
        }
        if (createCommitmentDto.getStartDate() != null && createCommitmentDto.getEndDate() != null) {
            commitmentBusinessRules.checkIfCommitmentDatesAreValid(
                createCommitmentDto.getStartDate(),
                createCommitmentDto.getEndDate()
            );
        }
        ContractDetail contractDetail = contractDetailService.findById(createCommitmentDto.getContractDetailId());
        
        // Check if the customer can make a new commitment
        commitmentBusinessRules.checkIfCustomerCanMakeNewCommitment(contractDetail.getCustomerId());
        
        Commitment commitment = new Commitment();
        commitment.setStatus(createCommitmentDto.getStatus());
        commitment.setDurationMonths(createCommitmentDto.getDurationMonths());
        commitment.setCreatedAt(LocalDateTime.now());
        commitment.setEndDate(createCommitmentDto.getEndDate());
        commitment.setStartDate(createCommitmentDto.getStartDate());
        commitment.setContractDetail(contractDetail);
        commitment.setEarlyTerminationFee(createCommitmentDto.getEarlyTerminationFee());
        commitmentRepository.save(commitment);
    }

    @Override
    public List<CommitmentListiningDto> getAll() {
        List<CommitmentListiningDto> commitmentListiningDtos=
                commitmentRepository
                        .findAll()
                        .stream()
                        .map(commitment -> new CommitmentListiningDto(commitment.getStatus(),commitment.getStartDate(),commitment.getEndDate(),commitment.getDurationMonths(),commitment.getContractDetail(),commitment.getEarlyTerminationFee())).toList();
        return commitmentListiningDtos;
    }

    @Override
    public Commitment update(UpdateCommitmentDto updateCommitmentDto) {
        ContractDetail contractDetail =contractDetailService.findById(updateCommitmentDto.getContractDetailId());
        Commitment commitment= commitmentRepository.findById(updateCommitmentDto.getId()).orElseThrow(()-> new BusinessException("Billing Plan not found with id: " + updateCommitmentDto.getId()));
        commitmentBusinessRules.checkIfCommitmentNameExistsForUpdate(
                updateCommitmentDto.getId(),
                updateCommitmentDto.getName()
        );
        if (updateCommitmentDto.getCycleType() != null && updateCommitmentDto.getBillingDay() != null) {
            commitmentBusinessRules.checkIfCycleTypeAndBillingDayAreConsistent(
                    updateCommitmentDto.getCycleType().toString(),
                    updateCommitmentDto.getBillingDay()
            );
        }
        if (updateCommitmentDto.getStartDate() != null && updateCommitmentDto.getEndDate() != null) {
            commitmentBusinessRules.checkIfCommitmentDatesAreValid(
                    updateCommitmentDto.getStartDate(),
                    updateCommitmentDto.getEndDate()
            );
        }

          commitment.setStatus(updateCommitmentDto.getStatus());
          commitment.setDurationMonths(updateCommitmentDto.getDurationMonths());
          commitment.setEndDate(updateCommitmentDto.getEndDate());
          commitment.setStartDate(updateCommitmentDto.getStartDate());
          commitment.setContractDetail(contractDetail);
          commitment.setEarlyTerminationFee(updateCommitmentDto.getEarlyTerminationFee());
          commitment.setUpdatedAt(LocalDateTime.now());
          commitmentRepository.save(commitment);
        return commitmentRepository.save(commitment);
    }

    @Override
    public void delete(DeleteCommitmentDto deleteCommitmentDto) {
        Commitment commitment=commitmentRepository.findById(deleteCommitmentDto.getId()).orElseThrow(()-> new BusinessException("Commitment not found with id: " + deleteCommitmentDto.getId()));
        commitmentRepository.delete(commitment);
    }
}
