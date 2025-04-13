package com.gygy.contractservice.mapper;

import com.gygy.contractservice.dto.discount.CreateDiscountDto;
import com.gygy.contractservice.dto.discount.DiscountListiningDto;
import com.gygy.contractservice.dto.discount.UpdateDiscountDto;
import com.gygy.contractservice.entity.Discount;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Data
public class DiscountMapper {
    public Discount createDiscountFromCreateDiscountDto(CreateDiscountDto createDiscountDto){
        return Discount.builder()
        .discountType(createDiscountDto.getDiscountType())
        .amount(createDiscountDto.getAmount())
        .percentage(createDiscountDto.getPercentage())
        .startDate(createDiscountDto.getStartDate())
        .endDate(createDiscountDto.getEndDate())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
        .status(createDiscountDto.getStatus())
                .description(createDiscountDto.getDescription())
                .billingCycleType(createDiscountDto.getBillingCycleType())
                .build();
    }
    public Discount updateDiscountFromUpdateDiscountDto(UpdateDiscountDto updateDiscountDto){
      return   Discount.builder()
        .discountType(updateDiscountDto.getDiscountType())
        .amount(updateDiscountDto.getAmount())
        .percentage(updateDiscountDto.getPercentage())
        .startDate(updateDiscountDto.getStartDate())
        .endDate(updateDiscountDto.getEndDate())
              .updatedAt(LocalDate.now())
              .createdAt(LocalDate.now())
              .status(updateDiscountDto.getStatus())
              .description(updateDiscountDto.getDescription())
              .billingCycleType(updateDiscountDto.getBillingCycleType())
              .build();
    }
    public DiscountListiningDto toDiscountListiningDto(Discount discount){
        return new DiscountListiningDto(
                discount.getDiscountType()
                ,discount.getAmount()
                ,discount.getPercentage()
                ,discount.getContract()
                ,discount.getEndDate()
                ,discount.getStartDate()
                ,discount.getCreatedAt(),
                discount.getUpdatedAt()
                ,discount.getStatus()
                ,discount.getDescription()
        );
    }

}
