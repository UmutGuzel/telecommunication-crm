package com.gygy.contractservice.dto.contract;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ContractListiningDto {
    private String contractNumber;
    private String documentType;
    private String documentUrl;
    private LocalDateTime uploadDate;
    private LocalDateTime signatureDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
