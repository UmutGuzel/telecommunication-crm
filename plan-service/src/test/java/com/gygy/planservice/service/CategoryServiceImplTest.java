package com.gygy.planservice.service;

import com.gygy.planservice.dto.CategoryDto;
import com.gygy.planservice.dto.CategoryRequestDto;
import com.gygy.planservice.entity.Category;
import com.gygy.planservice.exception.CategoryNotFoundException;
import com.gygy.planservice.exception.InvalidInputException;
import com.gygy.planservice.mapper.CategoryMapper;
import com.gygy.planservice.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private UUID categoryId;
    private Category category;
    private CategoryDto categoryDto;
    private CategoryRequestDto requestDto;

    @BeforeEach
    void setUp() {
        categoryId = UUID.randomUUID();
        
        category = new Category();
        category.setId(categoryId);
        category.setName("Test Category");
        category.setDescription("Test Description");
        category.setIsPassive(false);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        
        categoryDto = new CategoryDto();
        categoryDto.setId(categoryId);
        categoryDto.setName("Test Category");
        categoryDto.setDescription("Test Description");
        
        requestDto = new CategoryRequestDto();
        requestDto.setName("Test Category");
        requestDto.setDescription("Test Description");
    }

    @Test
    void createCategory_ValidRequest_ReturnsCategoryDto() {
        // Arrange
        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // Act
        CategoryDto result = categoryService.createCategory(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(categoryDto.getId(), result.getId());
        assertEquals(categoryDto.getName(), result.getName());
        assertEquals(categoryDto.getDescription(), result.getDescription());
        
        verify(categoryMapper).toEntity(requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void createCategory_NullRequest_ThrowsInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> categoryService.createCategory(null));
        
        verify(categoryMapper, never()).toEntity(any());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void createCategory_NullName_ThrowsInvalidInputException() {
        // Arrange
        requestDto.setName(null);
        
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> categoryService.createCategory(requestDto));
        
        verify(categoryMapper, never()).toEntity(any());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void getAllCategories_ReturnsAllCategories() {
        // Arrange
        List<Category> categories = Arrays.asList(category);
        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // Act
        List<CategoryDto> result = categoryService.getAllCategories();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(categoryDto, result.get(0));
        
        verify(categoryRepository).findAll();
        verify(categoryMapper).toDto(category);
    }

    @Test
    void getCategoryById_ExistingId_ReturnsCategoryDto() {
        // Arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // Act
        CategoryDto result = categoryService.getCategoryById(categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(categoryDto.getId(), result.getId());
        assertEquals(categoryDto.getName(), result.getName());
        assertEquals(categoryDto.getDescription(), result.getDescription());
        
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void getCategoryById_NullId_ThrowsInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> categoryService.getCategoryById(null));
        
        verify(categoryRepository, never()).findById(any());
    }

    @Test
    void getCategoryById_NonExistingId_ThrowsCategoryNotFoundException() {
        // Arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(categoryId));
        
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper, never()).toDto(any());
    }

    @Test
    void updateCategory_ValidRequest_ReturnsCategoryDto() {
        // Arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // Act
        CategoryDto result = categoryService.updateCategory(categoryId, requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(categoryDto.getId(), result.getId());
        assertEquals(categoryDto.getName(), result.getName());
        assertEquals(categoryDto.getDescription(), result.getDescription());
        
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void updateCategory_NullId_ThrowsInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> categoryService.updateCategory(null, requestDto));
        
        verify(categoryRepository, never()).findById(any());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateCategory_NullRequest_ThrowsInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> categoryService.updateCategory(categoryId, null));
        
        verify(categoryRepository, never()).findById(any());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateCategory_NullName_ThrowsInvalidInputException() {
        // Arrange
        requestDto.setName(null);
        
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> categoryService.updateCategory(categoryId, requestDto));
        
        verify(categoryRepository, never()).findById(any());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateCategory_NonExistingId_ThrowsCategoryNotFoundException() {
        // Arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(categoryId, requestDto));
        
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void deleteCategory_ExistingId_SetsIsPassiveTrue() {
        // Arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        // Act
        categoryService.deleteCategory(categoryId);

        // Assert
        assertTrue(category.getIsPassive());
        
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).save(category);
    }

    @Test
    void deleteCategory_NullId_ThrowsInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> categoryService.deleteCategory(null));
        
        verify(categoryRepository, never()).findById(any());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void deleteCategory_NonExistingId_ThrowsCategoryNotFoundException() {
        // Arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(categoryId));
        
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void getAllActiveCategories_ReturnsActiveCategories() {
        // Arrange
        List<Category> activeCategories = Arrays.asList(category);
        when(categoryRepository.findAllByIsPassiveFalse()).thenReturn(activeCategories);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // Act
        List<CategoryDto> result = categoryService.getAllActiveCategories();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(categoryDto, result.get(0));
        
        verify(categoryRepository).findAllByIsPassiveFalse();
        verify(categoryMapper).toDto(category);
    }
} 