package com.gygy.contractservice.mapper;

import com.gygy.contractservice.dto.contract.CreateContractDto;
import com.gygy.contractservice.dto.contract.UpdateContractDto;
import com.gygy.contractservice.entity.Contract;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
public class ContractMapper {
    public Contract createContractFromCreateContractDto(CreateContractDto createContractDto){
        return Contract.builder()
            .contractNumber(createContractDto.getContractNumber())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .documentType(createContractDto.getDocumentType())
            .signatureDate(createContractDto.getSignatureDate())
            .uploadDate(createContractDto.getUploadDate())
            .documentUrl(createContractDto.getDocumentUrl())
            .status(createContractDto.getStatus())
            .build();
    }
    public Contract updateContractFromUpdateContractDto(UpdateContractDto updateContractDto){
        return Contract.builder()
        .contractNumber(updateContractDto.getContractNumber())
        .status(updateContractDto.getStatus())
        .documentUrl(updateContractDto.getDocumentUrl())
        .signatureDate(updateContractDto.getSignatureDate())
        .uploadDate(updateContractDto.getUploadDate())
        .documentType(updateContractDto.getDocumentType())
                .id(updateContractDto.getId())
                .build();
    }
}
