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


    public ContractDetailListiningDto() {

    }


    public ContractDetailListiningDto(ContractDetailType contractDetailType, UUID id, Status status, LocalDate endDate, LocalDate startDate, UUID customerId, ServiceType serviceType) {
        this.contractDetailType=contractDetailType;
        this.contractId=id;
        this.status=status;
        this.endDate=endDate;
        this.startDate=startDate;
        this.customerId=customerId;
        this.serviceType=serviceType;

    }
}
