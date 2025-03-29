package com.gygy.contractservice.dto.commitment;

import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.model.enums.BillingCycleType;
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
    private String name;
    private Integer durationMonths;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double earlyTerminationFee;
    private Status status;
    private UUID contractDetailId;
    private Integer billingDay;
    private BillingCycleType cycleType;


    public CommitmentListiningDto() {

    }

    public CommitmentListiningDto(String name, Status status, Integer billingDay, BillingCycleType cycleType, LocalDate startDate, LocalDate endDate, Integer durationMonths, Double earlyTerminationFee,ContractDetail contractDetail) {
        this.name=name;
        this.status=status;
        this.billingDay=billingDay;
        this.cycleType=cycleType;
        this.startDate=startDate;
        this.endDate=endDate;
        this.durationMonths=durationMonths;
        this.earlyTerminationFee=earlyTerminationFee;
        this.contractDetailId = contractDetail.getId();

    }

}
