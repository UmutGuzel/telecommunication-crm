package com.gygy.contractservice.mapper;

import com.gygy.contractservice.dto.contract.ContractListiningDto;
import com.gygy.contractservice.dto.contract.CreateContractDto;
import com.gygy.contractservice.dto.contract.UpdateContractDto;
import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.model.enums.Status;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Data
public class ContractMapper {
    public Contract createContractFromCreateContractDto(CreateContractDto createContractDto) {
        return Contract.builder()
                .contractNumber(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .uploadDate(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public Contract updateContractFromUpdateContractDto(UpdateContractDto updateContractDto) {
        return Contract.builder()
                .contractNumber(updateContractDto.getContractNumber())
                .status(updateContractDto.getStatus())
                .documentUrl(updateContractDto.getDocumentUrl())
                .uploadDate(updateContractDto.getUploadDate())
                .documentType(updateContractDto.getDocumentType())
                .id(updateContractDto.getId())
                .build();
    }

    public ContractListiningDto toContractListiningDto(Contract contract) {
        return new ContractListiningDto(
                contract.getContractNumber(),
                contract.getStatus(),
                contract.getDocumentType(),
                contract.getDocumentUrl());
    }

}
