package com.gygy.contractservice.dto.commitment;

import com.gygy.contractservice.entity.ContractDetail;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CreateCommitmentDto {


    private Integer durationMonths;
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    @NotNull(message = "Early termination fee is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Early termination fee must be at least 0")
    private BigDecimal earlyTerminationFee;
    @NotNull(message = "Status is required")
    private String status;
    @NotNull(message = "Contract detail ID is required")
    private UUID contractDetailId;

}
