package com.gygy.contractservice.client;

import com.gygy.contractservice.dto.billingPlan.PlanDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name="plan-service",url="localhost:9040")
public interface PlanClient {
    @GetMapping("/api/plans/{id}")
    PlanDto getPlanById(@PathVariable  UUID id);
}
