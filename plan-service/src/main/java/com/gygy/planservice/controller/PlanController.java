package com.gygy.planservice.controller;

import com.gygy.planservice.dto.PlanDto;
import com.gygy.planservice.dto.PlanRequestDto;
import com.gygy.planservice.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return ResponseEntity.ok(planService.getPlanById(id));
    }

    @GetMapping
    public ResponseEntity<List<PlanDto>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanDto> updatePlan(@PathVariable UUID id, @RequestBody PlanRequestDto requestDto) {
        return ResponseEntity.ok(planService.updatePlan(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable UUID id) {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<PlanDto>> getAllActivePlans() {
        return ResponseEntity.ok(planService.getAllActivePlans());
    }
}