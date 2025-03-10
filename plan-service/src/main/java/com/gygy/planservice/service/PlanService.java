package com.gygy.planservice.service;

import com.gygy.planservice.dto.PlanDto;
import com.gygy.planservice.dto.PlanRequestDto;

import java.util.List;
import java.util.UUID;

public interface PlanService {
    PlanDto createPlan(PlanRequestDto requestDto);

    List<PlanDto> getAllPlans();

    PlanDto getPlanById(UUID id);

    PlanDto updatePlan(UUID id, PlanRequestDto requestDto);
}