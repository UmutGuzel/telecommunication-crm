package com.gygy.contractservice.dto.contractDetail;

import com.gygy.contractservice.model.enums.ContractDetailType;
import com.gygy.contractservice.model.enums.ContractStatus;
import com.gygy.contractservice.model.enums.ServiceType;
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
    private ContractStatus status;
    private ContractDetailType contractDetailType;
    private ServiceType serviceType;

    public ContractDetailListiningDto(ContractDetailType contractDetailType, UUID id, ContractStatus status, LocalDate endDate, LocalDate startDate) {
        this.contractDetailType = contractDetailType;
        this.contractId = id;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;

    }
}
