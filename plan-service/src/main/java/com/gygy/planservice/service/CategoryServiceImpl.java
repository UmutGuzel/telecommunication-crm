package com.gygy.planservice.service;

import com.gygy.planservice.dto.CategoryDto;
import com.gygy.planservice.dto.CategoryRequestDto;
import com.gygy.planservice.entity.Category;
import com.gygy.planservice.exception.CategoryNotFoundException;
import com.gygy.planservice.exception.CategoryIdNullException;
import com.gygy.planservice.exception.CategoryNameNullException;
import com.gygy.planservice.exception.CategoryRequestNullException;
import com.gygy.planservice.mapper.CategoryMapper;
import com.gygy.planservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryRequestDto requestDto) {
        if (requestDto == null) {
            throw new CategoryRequestNullException();
        }
        if (requestDto.getName() == null) {
            throw new CategoryNameNullException();
        }

        Category category = categoryMapper.toEntity(requestDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getCategoryById(UUID id) {
        if (id == null) {
            throw new CategoryIdNullException();
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(UUID id, CategoryRequestDto requestDto) {
        if (id == null) {
            throw new CategoryIdNullException();
        }
        if (requestDto == null) {
            throw new CategoryRequestNullException();
        }
        if (requestDto.getName() == null) {
            throw new CategoryNameNullException();
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        if (id == null) {
            throw new CategoryIdNullException();
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        category.setIsPassive(true);
        categoryRepository.save(category);
    }

    @Override
    public List<CategoryDto> getAllActiveCategories() {
        return categoryRepository.findAllByIsPassiveFalse().stream()
                .map(categoryMapper::toDto)
                .toList();
    }
}