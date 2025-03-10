package com.gygy.planservice.service;

import com.gygy.planservice.dto.PlanDto;
import com.gygy.planservice.dto.PlanRequestDto;

import java.util.List;

public interface PlanService {
    PlanDto createPlan(PlanRequestDto requestDto);

    List<PlanDto> getAllPlans();
}