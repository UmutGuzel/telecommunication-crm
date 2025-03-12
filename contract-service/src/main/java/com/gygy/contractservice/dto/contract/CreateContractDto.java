package com.gygy.contractservice.dto.contract;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateContractDto {
    @NotBlank(message = "Contract number is required")
    private String contractNumber;

    @NotBlank(message = "Document type is required")
    private String documentType;

    private String documentUrl;

    private LocalDateTime uploadDate;

    private LocalDateTime signatureDate;

    @NotNull(message = "Status is required")
    private String status;
}
