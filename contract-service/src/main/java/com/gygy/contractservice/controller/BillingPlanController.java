package com.gygy.contractservice.controller;

import com.gygy.contractservice.dto.billingPlan.BillingPlanListiningDto;
import com.gygy.contractservice.dto.billingPlan.CreateBillingPlanDto;
import com.gygy.contractservice.dto.billingPlan.DeleteBillingPlanDto;
import com.gygy.contractservice.dto.billingPlan.UpdateBillingPlanDto;
import com.gygy.contractservice.service.BillingPlanService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/billing-plans")
public class BillingPlanController {
    private final BillingPlanService billingPlanService;

    public BillingPlanController(BillingPlanService billingPlanService) {
        this.billingPlanService = billingPlanService;
    }
    @GetMapping
    public List<BillingPlanListiningDto> getAllBillingPlans() {
        return this.billingPlanService.getAll();
    }
    @PostMapping
    public void createBillingPlan(@RequestBody CreateBillingPlanDto createBillingPlanDto) {
        this.billingPlanService.add(createBillingPlanDto);
    }
    @PutMapping
    public void updateBillingPlan(@RequestBody UpdateBillingPlanDto updateBillingPlanDto) {
        this.billingPlanService.update(updateBillingPlanDto);
    }
    @DeleteMapping
    public void deleteBillingPlan(@RequestBody DeleteBillingPlanDto deleteBillingPlanDto) {
        this.billingPlanService.delete(deleteBillingPlanDto);
    }


}
