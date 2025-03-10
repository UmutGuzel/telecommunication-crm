package com.gygy.contractservice.dto.commitment;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
@Getter
@Setter
public class CommitmentListiningDto {
    private Integer durationMonths;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal earlyTerminationFee;
    private String status;
    private UUID contractDetailId;
}
