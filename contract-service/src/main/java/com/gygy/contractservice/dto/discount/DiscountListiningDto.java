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
    private String name;
    private String description;
    private double percentage;

    public DiscountListiningDto(String name, double percentage, String description) {
        this.name = name;
        this.percentage = percentage;
        this.description = description;
    }

    public DiscountListiningDto() {

    }
}
