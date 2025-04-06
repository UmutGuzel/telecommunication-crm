package com.gygy.contractservice.dto.contractDetail;

import com.gygy.contractservice.model.enums.ContractDetailType;
import com.gygy.contractservice.model.enums.ServiceType;
import com.gygy.contractservice.model.enums.Status;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UpdateContractDetailDto {
    private UUID id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Status status;
    private ServiceType serviceType;
    @NotNull(message = "Contract ID is required")
    private UUID contractId;
    private UUID customerId;
    @NotNull(message = "Contract detail type is required")
    private ContractDetailType contractDetailType;
    private LocalDateTime signatureDate;



}
