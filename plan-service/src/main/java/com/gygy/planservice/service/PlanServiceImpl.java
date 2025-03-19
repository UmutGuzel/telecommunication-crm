package com.gygy.planservice.service;

import com.gygy.planservice.dto.PlanDto;
import com.gygy.planservice.dto.PlanRequestDto;
import com.gygy.planservice.entity.Category;
import com.gygy.planservice.entity.Plan;
import com.gygy.planservice.exception.CategoryNotFoundException;
import com.gygy.planservice.exception.InvalidInputException;
import com.gygy.planservice.exception.PlanNotFoundException;
import com.gygy.planservice.mapper.PlanMapper;
import com.gygy.planservice.repository.CategoryRepository;
import com.gygy.planservice.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final CategoryRepository categoryRepository;
    private final PlanMapper planMapper;

    @Override
    @Transactional
    public PlanDto createPlan(PlanRequestDto requestDto) {
        if (requestDto == null) {
            throw new InvalidInputException("Plan request cannot be null");
        }
        if (requestDto.getCategoryId() == null) {
            throw new InvalidInputException("Category ID cannot be null");
        }

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with id: " + requestDto.getCategoryId()));

        Plan plan = planMapper.toEntity(requestDto, category);
        Plan savedPlan = planRepository.save(plan);
        return planMapper.toDto(savedPlan);
    }

    @Override
    public List<PlanDto> getAllPlans() {
        return planRepository.findAll().stream()
                .map(planMapper::toDto)
                .toList();
    }

    @Override
    public PlanDto getPlanById(UUID id) {
        if (id == null) {
            throw new InvalidInputException("Plan ID cannot be null");
        }

        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException("Plan not found with id: " + id));
        return planMapper.toDto(plan);
    }

    @Override
    @Transactional
    public PlanDto updatePlan(UUID id, PlanRequestDto requestDto) {
        if (id == null) {
            throw new InvalidInputException("Plan ID cannot be null");
        }
        if (requestDto == null) {
            throw new InvalidInputException("Plan request cannot be null");
        }
        if (requestDto.getCategoryId() == null) {
            throw new InvalidInputException("Category ID cannot be null");
        }

        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException("Plan not found with id: " + id));

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with id: " + requestDto.getCategoryId()));

        plan.setName(requestDto.getName());
        plan.setDescription(requestDto.getDescription());
        plan.setPrice(requestDto.getPrice());
        plan.setCategory(category);

        Plan updatedPlan = planRepository.save(plan);
        return planMapper.toDto(updatedPlan);
    }

    @Override
    @Transactional
    public void deletePlan(UUID id) {
        if (id == null) {
            throw new InvalidInputException("Plan ID cannot be null");
        }

        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException("Plan not found with id: " + id));

        plan.setIsPassive(true);
        planRepository.save(plan);
    }

    @Override
    public List<PlanDto> getAllActivePlans() {
        return planRepository.findAllByIsPassiveFalse().stream()
                .map(planMapper::toDto)
                .toList();
    }
}