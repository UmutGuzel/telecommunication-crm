package com.gygy.contractservice.service.impl;

import com.gygy.contractservice.dto.commitment.CommitmentListiningDto;
import com.gygy.contractservice.dto.commitment.CreateCommitmentDto;
import com.gygy.contractservice.dto.commitment.DeleteCommitmentDto;
import com.gygy.contractservice.dto.commitment.UpdateCommitmentDto;
import com.gygy.contractservice.entity.Commitment;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.repository.CommitmentRepository;
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

    public CommitmentServiceImpl(CommitmentRepository commitmentRepository,@Lazy ContractDetailService contractDetailService) {
        this.commitmentRepository = commitmentRepository;
        this.contractDetailService = contractDetailService;
    }

    @Override
    public Commitment findById(UUID id) {
        return commitmentRepository.findById(id).orElse(null);
    }

    @Override
    public void add(CreateCommitmentDto createCommitmentDto) {
        ContractDetail contractDetail = contractDetailService.findById(createCommitmentDto.getContractDetailId());
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
                        .map((commitment) -> new CommitmentListiningDto(commitment.getStatus(),commitment.getStartDate(),commitment.getEndDate(),commitment.getDurationMonths(),commitment.getContractDetail(),commitment.getEarlyTerminationFee())).toList();
        return commitmentListiningDtos;
    }

    @Override
    public Commitment update(UpdateCommitmentDto updateCommitmentDto) {
        ContractDetail contractDetail =contractDetailService.findById(updateCommitmentDto.getContractDetailId());
        Commitment commitment= commitmentRepository.findById(updateCommitmentDto.getId()).orElseThrow(()-> new RuntimeException("Commitment not found"));
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
        Commitment commitment = findById(deleteCommitmentDto.getId());
        commitmentRepository.delete(commitment);
    }
}
