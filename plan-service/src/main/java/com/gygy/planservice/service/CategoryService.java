package com.gygy.planservice.service;

import com.gygy.planservice.dto.CategoryDto;
import com.gygy.planservice.dto.CategoryRequestDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryRequestDto requestDto);

    List<CategoryDto> getAllCategories();
}