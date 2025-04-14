package com.gygy.contractservice.mapper;

import com.gygy.contractservice.dto.contractDetail.ContractDetailListiningDto;
import com.gygy.contractservice.dto.contractDetail.CreateContractDetailDto;
import com.gygy.contractservice.dto.contractDetail.UpdateContractDetailDto;
import com.gygy.contractservice.entity.ContractDetail;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
public class ContractDetailMapper {
    public ContractDetail createContractDetailFromCreateContractDetailDto(
            CreateContractDetailDto createContractDetailDto) {
        return ContractDetail.builder()
                .contractDetailType(createContractDetailDto.getContractDetailType())
                .serviceType(createContractDetailDto.getServiceType())
                .createdAt(LocalDateTime.now())
                .status(createContractDetailDto.getStatus())
                .email(createContractDetailDto.getEmail())
                .startDate(createContractDetailDto.getStartDate())
                .endDate(createContractDetailDto.getEndDate())
                .name(createContractDetailDto.getName())
                .signatureDate(createContractDetailDto.getSignatureDate())
                .customerName(createContractDetailDto.getCustomerName())
                .build();
    }

    public ContractDetail updateContractDetailFromUpdateContractDetailDto(
            UpdateContractDetailDto updateContractDetailDto) {
        return ContractDetail.builder()
                .serviceType(updateContractDetailDto.getServiceType())
                .startDate(updateContractDetailDto.getStartDate())
                .endDate(updateContractDetailDto.getEndDate())
                .updatedAt(LocalDateTime.now())
                .status(updateContractDetailDto.getStatus())
                .contractDetailType(updateContractDetailDto.getContractDetailType())
                .id(updateContractDetailDto.getId())
                .serviceType(updateContractDetailDto.getServiceType())
                .customerId(updateContractDetailDto.getCustomerId())
                .signatureDate(updateContractDetailDto.getSignatureDate())
                .build();
    }

    public ContractDetailListiningDto toContractDetailListiningDto(ContractDetail contractDetail) {
        return new ContractDetailListiningDto(
                contractDetail.getContractDetailType(), contractDetail.getContract().getId(),
                contractDetail.getStatus(), contractDetail.getEndDate(), contractDetail.getStartDate(),
                contractDetail.getCustomerId(), contractDetail.getServiceType(), contractDetail.getSignatureDate());
    }

}
