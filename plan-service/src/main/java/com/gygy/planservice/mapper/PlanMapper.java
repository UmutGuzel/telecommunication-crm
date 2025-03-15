package com.gygy.planservice.mapper;

import com.gygy.planservice.dto.PlanDto;
import com.gygy.planservice.dto.PlanRequestDto;
import com.gygy.planservice.entity.Category;
import com.gygy.planservice.entity.Plan;
import org.springframework.stereotype.Component;

@Component
public class PlanMapper {

    private final CategoryMapper categoryMapper;

    public PlanMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public Plan toEntity(PlanRequestDto dto, Category category) {
        Plan plan = new Plan();
        plan.setName(dto.getName());
        plan.setDescription(dto.getDescription());
        plan.setPrice(dto.getPrice());
        plan.setCategory(category);
        return plan;
    }

    public PlanDto toDto(Plan entity) {
        return new PlanDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                categoryMapper.toDto(entity.getCategory()));
    }
}