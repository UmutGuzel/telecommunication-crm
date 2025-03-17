package com.gygy.contractservice.dto.contractDetail;

import com.gygy.contractservice.model.enums.ContractDetailType;
import com.gygy.contractservice.model.enums.Status;
import com.gygy.contractservice.model.enums.ServiceType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CreateContractDetailDto {
    @NotNull(message = "Contract ID is required")
    private UUID contractId;
    private UUID customerId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Status is required")
    private Status status;

    @NotNull(message = "Contract detail type is required")
    private ContractDetailType contractDetailType;

    @NotNull(message = "Service type is required")
    private ServiceType serviceType;


}

