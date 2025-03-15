package com.gygy.contractservice.dto.contract;

import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.model.enums.ContractStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class ContractListiningDto {
    private String contractNumber;
    private String documentType;
    private String documentUrl;
    private LocalDateTime uploadDate;
    private LocalDateTime signatureDate;
    private ContractStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ContractListiningDto(String contractNumber, ContractStatus status, String documentType, String documentUrl, LocalDateTime signatureDate) {
        this.contractNumber = contractNumber;
        this.documentType = documentType;
        this.documentUrl = documentUrl;
        this.uploadDate = updatedAt;
        this.signatureDate = signatureDate;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
