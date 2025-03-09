package com.gygy.planservice.mapper;

import com.gygy.planservice.dto.CategoryDto;
import com.gygy.planservice.dto.CategoryRequestDto;
import com.gygy.planservice.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }

    public CategoryDto toDto(Category entity) {
        return new CategoryDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription());
    }
}