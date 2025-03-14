package com.gygy.contractservice.controller;

import com.gygy.contractservice.dto.discount.CreateDiscountDto;
import com.gygy.contractservice.dto.discount.DeleteDiscountDto;
import com.gygy.contractservice.dto.discount.DiscountListiningDto;
import com.gygy.contractservice.dto.discount.UpdateDiscountDto;
import com.gygy.contractservice.service.DiscountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discounts")
public class DiscountController {
    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }
    @GetMapping
    public List<DiscountListiningDto> getAll(){
        return discountService.getAll();
    }
    @PostMapping
    public void add(@RequestBody CreateDiscountDto createDiscountDto) {
        discountService.add(createDiscountDto);
    }
    @PutMapping
    public void update(@RequestBody UpdateDiscountDto updateDiscountDto) {
        discountService.update(updateDiscountDto);
    }
    @DeleteMapping
    public void delete(@RequestBody DeleteDiscountDto deleteDiscountDto) {
        discountService.delete(deleteDiscountDto);
    }
}
