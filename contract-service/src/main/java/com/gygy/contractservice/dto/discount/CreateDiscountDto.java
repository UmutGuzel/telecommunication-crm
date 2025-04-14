package com.gygy.contractservice.dto.discount;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDiscountDto {
    private String name;
    @NotNull(message = "Description is required")
    private String description;
    @Min(value = 0, message = "Percentage must be greater than or equal to 0")
    private double percentage;

}
