package com.gygy.contractservice.dto.discount;

import com.gygy.contractservice.model.enums.BillingCycleType;
import com.gygy.contractservice.model.enums.DiscountType;
import com.gygy.contractservice.model.enums.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateDiscountDto {
    private String name;
    @NotNull(message = "Description is required")
    private String description;
    @Min(value = 0, message = "Percentage must be greater than or equal to 0")
    private double percentage;

}
