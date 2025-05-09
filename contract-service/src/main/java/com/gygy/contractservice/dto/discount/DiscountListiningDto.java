package com.gygy.contractservice.dto.discount;

import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.model.enums.DiscountType;
import com.gygy.contractservice.model.enums.Status;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class DiscountListiningDto {
    private UUID contractId;
    private DiscountType discountType;
    private double amount;
    private double percentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private  List<UUID> billingPlanId;
    private LocalDate createDate;
    private LocalDate updateDate;
    private Status status;


    public DiscountListiningDto(DiscountType discountType, double amount, double percentage, Contract contract, LocalDate endDate, LocalDate startDate, LocalDate createdAt, LocalDate updatedAt, Status status, String description) {
        this.discountType = discountType;
        this.amount = amount;
        this.percentage = percentage;
        this.contractId = contract.getId();
        this.endDate = endDate;
        this.startDate = startDate;
        this.createDate = createdAt;
        this.updateDate = updatedAt;
        this.status=status;
        this.description=description;
    }

    public DiscountListiningDto() {

    }
}
