package com.gygy.contractservice.dto.contractDetail;

import com.gygy.contractservice.model.enums.ContractDetailType;
import com.gygy.contractservice.model.enums.Status;
import com.gygy.contractservice.model.enums.ServiceType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ContractDetailListiningDto {
    private UUID id;
    private UUID contractId;
    private UUID customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Status status;
    private ContractDetailType contractDetailType;
    private ServiceType serviceType;

    public ContractDetailListiningDto(ContractDetailType contractDetailType, UUID id, Status status, LocalDate endDate, LocalDate startDate) {
        this.contractDetailType = contractDetailType;
        this.contractId = id;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;

    }

    public ContractDetailListiningDto(ContractDetailType contractDetailType, ServiceType serviceType, LocalDate startDate, UUID id, LocalDate endDate, Status status) {
        this.contractDetailType=contractDetailType;
        this.serviceType=serviceType;
        this.startDate=startDate;
        this.endDate=endDate;
        this.contractId=id;
        this.status=status;
    }

    public ContractDetailListiningDto() {

    }
}
