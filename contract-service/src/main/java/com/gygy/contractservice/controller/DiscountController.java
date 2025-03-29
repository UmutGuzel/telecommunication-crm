package com.gygy.contractservice.controller;

import com.gygy.contractservice.dto.discount.CreateDiscountDto;
import com.gygy.contractservice.dto.discount.DeleteDiscountDto;
import com.gygy.contractservice.dto.discount.DiscountListiningDto;
import com.gygy.contractservice.dto.discount.UpdateDiscountDto;
import com.gygy.contractservice.entity.Discount;
import com.gygy.contractservice.service.DiscountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    @GetMapping("/getActiveDiscounts")
    public List<DiscountListiningDto> getActiveDiscounts(){
        return discountService.getActiveDiscounts();
    }
    @GetMapping("/get-active-discountsByCustomerId")
    public List<DiscountListiningDto> getActiveDiscountsByCustomerId(@RequestParam UUID id){
        return discountService.getActiveDiscountsByCustomerId(id);
    }
    @GetMapping("/getDiscountsByContractId")
    public List<DiscountListiningDto> getDiscountsByContractId(@RequestParam UUID id){
        return discountService.getDiscountsByContractId(id);
    }
    @PostMapping("/discounted")
    public Discount createDiscountForAnnualPackage(@RequestBody CreateDiscountDto createDiscountDto) {
        Discount createdDiscount = discountService.applyDiscountForAnnualPackage(createDiscountDto);
        return createdDiscount;
    }
}
