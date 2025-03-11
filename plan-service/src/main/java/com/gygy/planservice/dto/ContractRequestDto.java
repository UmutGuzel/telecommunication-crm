package com.gygy.planservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequestDto {
    private String type;
    private BigDecimal discount;
    private UUID planId;
}