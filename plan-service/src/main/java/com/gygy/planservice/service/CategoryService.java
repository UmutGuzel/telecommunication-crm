package com.gygy.planservice.service;

import com.gygy.planservice.dto.CategoryDto;
import com.gygy.planservice.dto.CategoryRequestDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryDto createCategory(CategoryRequestDto requestDto);

    List<CategoryDto> getAllCategories();

    CategoryDto getCategoryById(UUID id);

    CategoryDto updateCategory(UUID id, CategoryRequestDto requestDto);

    void deleteCategory(UUID id);
}