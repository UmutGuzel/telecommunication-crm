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
    private String name;
    private String description;
    private double percentage;

    public DiscountListiningDto(double percentage, Contract contract, LocalDate endDate, LocalDate startDate,
            LocalDate createdAt, LocalDate updatedAt, UUID customerId, Status status, String description) {
        this.percentage = percentage;
        this.contractId = contract.getId();
        this.name = name;
        this.description = description;
    }

    public DiscountListiningDto() {

    }
}
