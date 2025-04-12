package com.gygy.contractservice.dto.contractDetail;

import com.gygy.contractservice.model.enums.ContractDetailType;
import com.gygy.contractservice.model.enums.Status;
import com.gygy.contractservice.model.enums.ServiceType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CreateContractDetailDto {
    @NotNull(message = "Contract ID is required")
    private UUID contractId;
    //private UUID customerId;
    private UUID billingPlanId;
    private UUID discountId;
    private String name;
    private String customerName;
   // private String phoneNumber;
    private String email;



    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Status is required")
    private Status status;

    private LocalDateTime signatureDate;


    @NotNull(message = "Contract detail type is required")
    private ContractDetailType contractDetailType;

    @NotNull(message = "Service type is required")
    private ServiceType serviceType;


}

