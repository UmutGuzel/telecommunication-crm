package com.gygy.planservice.controller;

import com.gygy.planservice.dto.PlanDto;
import com.gygy.planservice.dto.PlanRequestDto;
import com.gygy.planservice.service.PlanService;
import com.gygy.planservice.exception.PlanNotFoundException;
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

class PlanControllerTest {

    @Mock
    private PlanService planService;

    @InjectMocks
    private PlanController planController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPlan() {
        PlanRequestDto requestDto = new PlanRequestDto();
        PlanDto planDto = new PlanDto();
        when(planService.createPlan(any(PlanRequestDto.class))).thenReturn(planDto);

        ResponseEntity<PlanDto> response = planController.createPlan(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(planDto, response.getBody());
        verify(planService).createPlan(requestDto);
    }

    @Test
    void getPlanById() {
        UUID id = UUID.randomUUID();
        PlanDto planDto = new PlanDto();
        when(planService.getPlanById(id)).thenReturn(planDto);

        ResponseEntity<PlanDto> response = planController.getPlanById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(planDto, response.getBody());
        verify(planService).getPlanById(id);
    }

    @Test
    void getPlanById_NotFound() {
        UUID id = UUID.randomUUID();
        when(planService.getPlanById(id)).thenThrow(new PlanNotFoundException("Plan not found"));

        PlanNotFoundException exception = assertThrows(PlanNotFoundException.class, () -> {
            planController.getPlanById(id);
        });

        assertEquals("Plan not found", exception.getMessage());
        verify(planService).getPlanById(id);
    }

    @Test
    void getAllPlans() {
        when(planService.getAllPlans()).thenReturn(Collections.emptyList());

        ResponseEntity<List<PlanDto>> response = planController.getAllPlans();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(planService).getAllPlans();
    }

    @Test
    void updatePlan() {
        UUID id = UUID.randomUUID();
        PlanRequestDto requestDto = new PlanRequestDto();
        PlanDto planDto = new PlanDto();
        when(planService.updatePlan(eq(id), any(PlanRequestDto.class))).thenReturn(planDto);

        ResponseEntity<PlanDto> response = planController.updatePlan(id, requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(planDto, response.getBody());
        verify(planService).updatePlan(id, requestDto);
    }

    @Test
    void updatePlan_NotFound() {
        UUID id = UUID.randomUUID();
        PlanRequestDto requestDto = new PlanRequestDto();
        when(planService.updatePlan(eq(id), any(PlanRequestDto.class)))
                .thenThrow(new PlanNotFoundException("Plan not found"));

        PlanNotFoundException exception = assertThrows(PlanNotFoundException.class, () -> {
            planController.updatePlan(id, requestDto);
        });

        assertEquals("Plan not found", exception.getMessage());
        verify(planService).updatePlan(id, requestDto);
    }

    @Test
    void deletePlan() {
        UUID id = UUID.randomUUID();
        doNothing().when(planService).deletePlan(id);

        ResponseEntity<Void> response = planController.deletePlan(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(planService).deletePlan(id);
    }

    @Test
    void getAllActivePlans() {
        when(planService.getAllActivePlans()).thenReturn(Collections.emptyList());

        ResponseEntity<List<PlanDto>> response = planController.getAllActivePlans();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(planService).getAllActivePlans();
    }
} 