package com.gygy.contractservice.service;

import com.gygy.contractservice.dto.contract.UpdateContractDto;
import com.gygy.contractservice.dto.discount.CreateDiscountDto;
import com.gygy.contractservice.dto.discount.DeleteDiscountDto;
import com.gygy.contractservice.dto.discount.DiscountListiningDto;
import com.gygy.contractservice.dto.discount.UpdateDiscountDto;
import com.gygy.contractservice.entity.Discount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiscountService {
    List<Discount> findAllById(List<UUID> ids);
    Discount findById(UUID id);
    void add(CreateDiscountDto createDiscountDto);
    List<DiscountListiningDto> getAll();
    Discount update(UpdateDiscountDto updateDiscountDto);
    void delete(DeleteDiscountDto deleteDiscountDto);
}
