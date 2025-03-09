package com.gygy.planservice.controller;

import com.gygy.planservice.dto.PlanDto;
import com.gygy.planservice.dto.PlanRequestDto;
import com.gygy.planservice.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping
    public ResponseEntity<PlanDto> createPlan(@RequestBody PlanRequestDto requestDto) {
        PlanDto createdPlan = planService.createPlan(requestDto);
        return new ResponseEntity<>(createdPlan, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanDto> getPlanById(@PathVariable UUID id) {
        // TODO add the getPlanById method to the service
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllPlans() {
        // TODO add the getAllPlans method to the service
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlan(@PathVariable UUID id, @RequestBody PlanRequestDto requestDto) {
        // TODO add the updatePlan method to the service
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlan(@PathVariable UUID id) {
        // TODO add the deletePlan method to the service
        return ResponseEntity.noContent().build();
    }
}