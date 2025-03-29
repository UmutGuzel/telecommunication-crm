package com.gygy.contractservice.mapper;

import com.gygy.contractservice.dto.commitment.CommitmentListiningDto;
import com.gygy.contractservice.dto.commitment.CreateCommitmentDto;
import com.gygy.contractservice.dto.commitment.UpdateCommitmentDto;
import com.gygy.contractservice.entity.Commitment;
import com.gygy.contractservice.repository.CommitmentRepository;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Data
public class CommitmentMapper {
    public Commitment createCommitmentFromCreateCommitmentDto(CreateCommitmentDto createCommitmentDto){
        return Commitment.builder()
                .name(createCommitmentDto.getName())
                .createdAt(LocalDateTime.now())
                .billingDay(createCommitmentDto.getBillingDay())
                .endDate(createCommitmentDto.getEndDate())
                .status(createCommitmentDto.getStatus())
                .cycleType(createCommitmentDto.getCycleType())
                .startDate(createCommitmentDto.getStartDate())
                .durationMonths(createCommitmentDto.getDurationMonths())
                .earlyTerminationFee(createCommitmentDto.getEarlyTerminationFee())
                .build();
    }
    public Commitment updateCommitmentFromUpdateCommitmentDto(UpdateCommitmentDto updateCommitmentDto){
        return Commitment.builder()
                .status(updateCommitmentDto.getStatus())
                .durationMonths(updateCommitmentDto.getDurationMonths())
                .startDate(updateCommitmentDto.getEndDate())
                .endDate(updateCommitmentDto.getEndDate())
                .earlyTerminationFee(updateCommitmentDto.getEarlyTerminationFee())
                .updatedAt(LocalDateTime.now())
                .billingDay(updateCommitmentDto.getBillingDay())
                .cycleType(updateCommitmentDto.getCycleType())
                .name(updateCommitmentDto.getName())
                .build();
    }





}
