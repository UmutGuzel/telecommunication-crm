package com.gygy.contractservice.dto.contractDetail;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ContractDetailListiningDto {
    private UUID contractId;
    private UUID customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String contractDetailType;
    private String serviceType;

}
