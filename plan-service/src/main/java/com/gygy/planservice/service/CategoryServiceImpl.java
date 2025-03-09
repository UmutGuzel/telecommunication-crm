package com.gygy.planservice.service;

import com.gygy.planservice.dto.CategoryDto;
import com.gygy.planservice.dto.CategoryRequestDto;
import com.gygy.planservice.entity.Category;
import com.gygy.planservice.mapper.CategoryMapper;
import com.gygy.planservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryRequestDto requestDto) {
        Category category = categoryMapper.toEntity(requestDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }
}