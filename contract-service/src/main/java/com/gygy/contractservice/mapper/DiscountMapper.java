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
    public Discount createDiscountFromCreateDiscountDto(CreateDiscountDto createDiscountDto) {
        return Discount.builder()
                .name(createDiscountDto.getName())
                .percentage(createDiscountDto.getPercentage())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .description(createDiscountDto.getDescription())
                .build();
    }

    public Discount updateDiscountFromUpdateDiscountDto(UpdateDiscountDto updateDiscountDto) {
        return Discount.builder()
                .name(updateDiscountDto.getName())
                .percentage(updateDiscountDto.getPercentage())
                .description(updateDiscountDto.getDescription())
                .updatedAt(LocalDate.now())
                .createdAt(LocalDate.now())
                .build();
    }

    public DiscountListiningDto toDiscountListiningDto(Discount discount) {
        return new DiscountListiningDto(
                discount.getPercentage(), discount.getContract(), discount.getEndDate(), discount.getStartDate(),
                discount.getCreatedAt(), discount.getUpdatedAt(), discount.getCustomerId(), discount.getStatus(),
                discount.getDescription());
    }
}
