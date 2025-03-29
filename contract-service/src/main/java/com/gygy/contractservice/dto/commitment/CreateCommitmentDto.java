package com.gygy.contractservice.dto.commitment;

import com.gygy.contractservice.model.enums.BillingCycleType;
import com.gygy.contractservice.model.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CreateCommitmentDto {


    private String name;
    private Integer durationMonths;
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    private BillingCycleType cycleType;

    private Integer billingDay;

    private Double earlyTerminationFee;
    @NotNull(message = "Status is required")
    private Status status;
    @NotNull(message = "Contract detail ID is required")
    private UUID contractDetailId;

}
