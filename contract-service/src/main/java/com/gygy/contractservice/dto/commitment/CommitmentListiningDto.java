package com.gygy.contractservice.dto.commitment;

import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.model.enums.Status;
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
    private Double earlyTerminationFee;
    private Status status;
    private UUID contractDetailId;

    public CommitmentListiningDto(Status status, LocalDate startDate, LocalDate endDate, Integer durationMonths, ContractDetail contractDetail, Double earlyTerminationFee) {
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.durationMonths = durationMonths;
        this.contractDetailId = contractDetail.getId();
        this.earlyTerminationFee = earlyTerminationFee;
    }

    public CommitmentListiningDto() {

    }
}
