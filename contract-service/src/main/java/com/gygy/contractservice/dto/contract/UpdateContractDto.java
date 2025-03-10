package com.gygy.contractservice.dto.contract;

import com.gygy.contractservice.model.enums.ContractStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UpdateContractDto {
    private UUID id;
    private String contractNumber;
    private String documentType;
    private String documentUrl;
    private LocalDateTime uploadDate;
    private LocalDateTime signatureDate;
    private String status;
}
