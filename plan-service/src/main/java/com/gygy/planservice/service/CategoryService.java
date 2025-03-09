package com.gygy.planservice.service;

import com.gygy.planservice.dto.CategoryDto;
import com.gygy.planservice.dto.CategoryRequestDto;

public interface CategoryService {
    CategoryDto createCategory(CategoryRequestDto requestDto);
}