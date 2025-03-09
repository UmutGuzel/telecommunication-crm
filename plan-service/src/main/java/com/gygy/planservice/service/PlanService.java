package com.gygy.planservice.service;

import com.gygy.planservice.dto.PlanDto;
import com.gygy.planservice.dto.PlanRequestDto;

public interface PlanService {
    PlanDto createPlan(PlanRequestDto requestDto);
}