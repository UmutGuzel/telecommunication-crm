package com.gygy.planservice.controller;

import com.gygy.planservice.dto.CategoryDto;
import com.gygy.planservice.dto.CategoryRequestDto;
import com.gygy.planservice.service.CategoryService;
import com.gygy.planservice.exception.CategoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories() {
        when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());

        ResponseEntity<List<CategoryDto>> response = categoryController.getAllCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(categoryService).getAllCategories();
    }

    @Test
    void getCategoryById() {
        UUID id = UUID.randomUUID();
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.getCategoryById(id)).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> response = categoryController.getCategoryById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoryDto, response.getBody());
        verify(categoryService).getCategoryById(id);
    }

    @Test
    void getCategoryById_NotFound() {
        UUID id = UUID.randomUUID();
        when(categoryService.getCategoryById(id)).thenThrow(new CategoryNotFoundException("Category not found"));

        // Use assertThrows to check for the exception
        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            categoryController.getCategoryById(id);
        });

        // Assert the exception message
        assertEquals("Category not found", exception.getMessage());
        verify(categoryService).getCategoryById(id);
    }

    @Test
    void createCategory() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.createCategory(any(CategoryRequestDto.class))).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> response = categoryController.createCategory(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(categoryDto, response.getBody());
        verify(categoryService).createCategory(requestDto);
    }

    @Test
    void updateCategory() {
        UUID id = UUID.randomUUID();
        CategoryRequestDto requestDto = new CategoryRequestDto();
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.updateCategory(eq(id), any(CategoryRequestDto.class))).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> response = categoryController.updateCategory(id, requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoryDto, response.getBody());
        verify(categoryService).updateCategory(id, requestDto);
    }

    @Test
    void updateCategory_NotFound() {
        UUID id = UUID.randomUUID();
        CategoryRequestDto requestDto = new CategoryRequestDto();
        when(categoryService.updateCategory(eq(id), any(CategoryRequestDto.class)))
                .thenThrow(new CategoryNotFoundException("Category not found"));

        // Use assertThrows to check for the exception
        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            categoryController.updateCategory(id, requestDto);
        });

        // Assert the exception message
        assertEquals("Category not found", exception.getMessage());
        verify(categoryService).updateCategory(id, requestDto);
    }

    @Test
    void deleteCategory() {
        UUID id = UUID.randomUUID();
        doNothing().when(categoryService).deleteCategory(id);

        ResponseEntity<Void> response = categoryController.deleteCategory(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(categoryService).deleteCategory(id);
    }

    @Test
    void getAllActiveCategories() {
        when(categoryService.getAllActiveCategories()).thenReturn(Collections.emptyList());

        ResponseEntity<List<CategoryDto>> response = categoryController.getAllActiveCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(categoryService).getAllActiveCategories();
    }
} 