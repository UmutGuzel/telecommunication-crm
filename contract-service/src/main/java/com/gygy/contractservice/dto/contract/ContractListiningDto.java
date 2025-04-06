package com.gygy.contractservice.dto.contract;

import com.gygy.contractservice.model.enums.Status;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ContractListiningDto {
    private String contractNumber;
    private String documentType;
    private String documentUrl;
    private LocalDateTime uploadDate;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ContractListiningDto(String contractNumber, Status status, String documentType, String documentUrl) {
        this.contractNumber = contractNumber;
        this.documentType = documentType;
        this.documentUrl = documentUrl;
        this.uploadDate = updatedAt;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public ContractListiningDto() {

    }



}
