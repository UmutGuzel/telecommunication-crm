package com.gygy.planservice.service;

import com.gygy.planservice.dto.CategoryDto;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanServiceImplTest {

    @Mock
    private PlanRepository planRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PlanMapper planMapper;

    @InjectMocks
    private PlanServiceImpl planService;

    private UUID planId;
    private UUID categoryId;
    private Plan plan;
    private Category category;
    private CategoryDto categoryDto;
    private PlanDto planDto;
    private PlanRequestDto requestDto;

    @BeforeEach
    void setUp() {
        planId = UUID.randomUUID();
        categoryId = UUID.randomUUID();
        
        category = new Category();
        category.setId(categoryId);
        category.setName("Test Category");
        category.setDescription("Test Description");
        category.setIsPassive(false);
        
        categoryDto = new CategoryDto();
        categoryDto.setId(categoryId);
        categoryDto.setName("Test Category");
        categoryDto.setDescription("Test Description");
        
        plan = new Plan();
        plan.setId(planId);
        plan.setName("Test Plan");
        plan.setDescription("Test Description");
        plan.setPrice(BigDecimal.valueOf(99.99));
        plan.setCategory(category);
        plan.setIsPassive(false);
        plan.setCreatedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());
        
        planDto = new PlanDto();
        planDto.setId(planId);
        planDto.setName("Test Plan");
        planDto.setDescription("Test Description");
        planDto.setPrice(BigDecimal.valueOf(99.99));
        planDto.setCategory(categoryDto);
        
        requestDto = new PlanRequestDto();
        requestDto.setName("Test Plan");
        requestDto.setDescription("Test Description");
        requestDto.setPrice(BigDecimal.valueOf(99.99));
        requestDto.setCategoryId(categoryId);
    }

    @Test
    void createPlan_ValidRequest_ReturnsPlanDto() {
        // Arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(planMapper.toEntity(requestDto, category)).thenReturn(plan);
        when(planRepository.save(plan)).thenReturn(plan);
        when(planMapper.toDto(plan)).thenReturn(planDto);

        // Act
        PlanDto result = planService.createPlan(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(planDto.getId(), result.getId());
        assertEquals(planDto.getName(), result.getName());
        assertEquals(planDto.getDescription(), result.getDescription());
        assertEquals(planDto.getPrice(), result.getPrice());
        assertEquals(planDto.getCategory().getId(), result.getCategory().getId());
        
        verify(categoryRepository).findById(categoryId);
        verify(planMapper).toEntity(requestDto, category);
        verify(planRepository).save(plan);
        verify(planMapper).toDto(plan);
    }

    @Test
    void createPlan_NullRequest_ThrowsInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> planService.createPlan(null));
        
        verify(categoryRepository, never()).findById(any());
        verify(planMapper, never()).toEntity(any(), any());
        verify(planRepository, never()).save(any());
    }

    @Test
    void createPlan_NullCategoryId_ThrowsInvalidInputException() {
        // Arrange
        requestDto.setCategoryId(null);
        
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> planService.createPlan(requestDto));
        
        verify(categoryRepository, never()).findById(any());
        verify(planMapper, never()).toEntity(any(), any());
        verify(planRepository, never()).save(any());
    }

    @Test
    void createPlan_NonExistingCategoryId_ThrowsCategoryNotFoundException() {
        // Arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> planService.createPlan(requestDto));
        
        verify(categoryRepository).findById(categoryId);
        verify(planMapper, never()).toEntity(any(), any());
        verify(planRepository, never()).save(any());
    }

    @Test
    void getAllPlans_ReturnsAllPlans() {
        // Arrange
        List<Plan> plans = Arrays.asList(plan);
        when(planRepository.findAll()).thenReturn(plans);
        when(planMapper.toDto(plan)).thenReturn(planDto);

        // Act
        List<PlanDto> result = planService.getAllPlans();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(planDto, result.get(0));
        
        verify(planRepository).findAll();
        verify(planMapper).toDto(plan);
    }

    @Test
    void getPlanById_ExistingId_ReturnsPlanDto() {
        // Arrange
        when(planRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(planMapper.toDto(plan)).thenReturn(planDto);

        // Act
        PlanDto result = planService.getPlanById(planId);

        // Assert
        assertNotNull(result);
        assertEquals(planDto.getId(), result.getId());
        assertEquals(planDto.getName(), result.getName());
        assertEquals(planDto.getDescription(), result.getDescription());
        assertEquals(planDto.getPrice(), result.getPrice());
        assertEquals(planDto.getCategory().getId(), result.getCategory().getId());
        
        verify(planRepository).findById(planId);
        verify(planMapper).toDto(plan);
    }

    @Test
    void getPlanById_NullId_ThrowsInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> planService.getPlanById(null));
        
        verify(planRepository, never()).findById(any());
    }

    @Test
    void getPlanById_NonExistingId_ThrowsPlanNotFoundException() {
        // Arrange
        when(planRepository.findById(planId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlanNotFoundException.class, () -> planService.getPlanById(planId));
        
        verify(planRepository).findById(planId);
        verify(planMapper, never()).toDto(any());
    }

    @Test
    void updatePlan_ValidRequest_ReturnsPlanDto() {
        // Arrange
        when(planRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(planRepository.save(plan)).thenReturn(plan);
        when(planMapper.toDto(plan)).thenReturn(planDto);

        // Act
        PlanDto result = planService.updatePlan(planId, requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(planDto.getId(), result.getId());
        assertEquals(planDto.getName(), result.getName());
        assertEquals(planDto.getDescription(), result.getDescription());
        assertEquals(planDto.getPrice(), result.getPrice());
        assertEquals(planDto.getCategory().getId(), result.getCategory().getId());
        
        verify(planRepository).findById(planId);
        verify(categoryRepository).findById(categoryId);
        verify(planRepository).save(plan);
        verify(planMapper).toDto(plan);
    }

    @Test
    void updatePlan_NullId_ThrowsInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> planService.updatePlan(null, requestDto));
        
        verify(planRepository, never()).findById(any());
        verify(categoryRepository, never()).findById(any());
        verify(planRepository, never()).save(any());
    }

    @Test
    void updatePlan_NullRequest_ThrowsInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> planService.updatePlan(planId, null));
        
        verify(planRepository, never()).findById(any());
        verify(categoryRepository, never()).findById(any());
        verify(planRepository, never()).save(any());
    }

    @Test
    void updatePlan_NullCategoryId_ThrowsInvalidInputException() {
        // Arrange
        requestDto.setCategoryId(null);
        
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> planService.updatePlan(planId, requestDto));
        
        verify(planRepository, never()).findById(any());
        verify(categoryRepository, never()).findById(any());
        verify(planRepository, never()).save(any());
    }

    @Test
    void updatePlan_NonExistingPlanId_ThrowsPlanNotFoundException() {
        // Arrange
        when(planRepository.findById(planId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlanNotFoundException.class, () -> planService.updatePlan(planId, requestDto));
        
        verify(planRepository).findById(planId);
        verify(categoryRepository, never()).findById(any());
        verify(planRepository, never()).save(any());
    }

    @Test
    void updatePlan_NonExistingCategoryId_ThrowsCategoryNotFoundException() {
        // Arrange
        when(planRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> planService.updatePlan(planId, requestDto));
        
        verify(planRepository).findById(planId);
        verify(categoryRepository).findById(categoryId);
        verify(planRepository, never()).save(any());
    }

    @Test
    void deletePlan_ExistingId_SetsIsPassiveTrue() {
        // Arrange
        when(planRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(planRepository.save(plan)).thenReturn(plan);

        // Act
        planService.deletePlan(planId);

        // Assert
        assertTrue(plan.getIsPassive());
        
        verify(planRepository).findById(planId);
        verify(planRepository).save(plan);
    }

    @Test
    void deletePlan_NullId_ThrowsInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> planService.deletePlan(null));
        
        verify(planRepository, never()).findById(any());
        verify(planRepository, never()).save(any());
    }

    @Test
    void deletePlan_NonExistingId_ThrowsPlanNotFoundException() {
        // Arrange
        when(planRepository.findById(planId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlanNotFoundException.class, () -> planService.deletePlan(planId));
        
        verify(planRepository).findById(planId);
        verify(planRepository, never()).save(any());
    }

    @Test
    void getAllActivePlans_ReturnsActivePlans() {
        // Arrange
        List<Plan> activePlans = Arrays.asList(plan);
        when(planRepository.findAllByIsPassiveFalse()).thenReturn(activePlans);
        when(planMapper.toDto(plan)).thenReturn(planDto);

        // Act
        List<PlanDto> result = planService.getAllActivePlans();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(planDto, result.get(0));
        
        verify(planRepository).findAllByIsPassiveFalse();
        verify(planMapper).toDto(plan);
    }
} 