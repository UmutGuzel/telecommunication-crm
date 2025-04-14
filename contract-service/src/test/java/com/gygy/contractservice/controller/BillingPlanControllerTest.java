package com.gygy.contractservice.controller;

import com.gygy.contractservice.core.exception.type.BusinessException;
import com.gygy.contractservice.dto.billingPlan.BillingPlanListiningDto;
import com.gygy.contractservice.dto.billingPlan.CreateBillingPlanDto;
import com.gygy.contractservice.dto.billingPlan.DeleteBillingPlanDto;
import com.gygy.contractservice.dto.billingPlan.UpdateBillingPlanDto;
import com.gygy.contractservice.entity.BillingPlan;
import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.rules.BillingPlanBusinessRules;
import com.gygy.contractservice.service.BillingPlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.gygy.contractservice.model.enums.BillingCycleType.MONTHLY;
import static com.gygy.contractservice.model.enums.PaymentMethod.CREDIT_CARD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BillingPlanControllerTest {
    @Mock
    private BillingPlanService billingPlanService;
    @Mock
    private BillingPlanBusinessRules billingPlanBusinessRules;

    @InjectMocks
    private BillingPlanController billingPlanController;

    private UUID id;
    private Contract contract;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addBillingPlan() {
        CreateBillingPlanDto createBillingPlanDto = new CreateBillingPlanDto();
        // doNothing().when(billingPlanService).add(any(CreateBillingPlanDto.class));

        // billingPlanController.createBillingPlan(createBillingPlanDto);

        // verify(billingPlanService,times(1)).add(any(CreateBillingPlanDto.class));
    }

    @Test
    void getAllBillingPlan() {
        contract = new Contract();
        contract.setId(UUID.randomUUID());
        BillingPlanListiningDto billingPlanListiningDto = new BillingPlanListiningDto();
        // billingPlanListiningDto.setBillingDay(12);
        billingPlanListiningDto.setName("Test Name");
        billingPlanListiningDto.setDescription("Test Description");
        billingPlanListiningDto.setBaseAmount(1000);
        billingPlanListiningDto.setCycleType(MONTHLY);
        // billingPlanListiningDto.setPaymentDueDays(12);
        billingPlanListiningDto.setTaxRate(0.1);
        billingPlanListiningDto.setPaymentMethod(CREDIT_CARD);
        billingPlanListiningDto.setContractId(contract.getId());

        List<BillingPlanListiningDto> billingPlanListiningDtos = List.of(billingPlanListiningDto);

        when(billingPlanService.getAll()).thenReturn(billingPlanListiningDtos);
        // Act
        List<BillingPlanListiningDto> response = billingPlanController.getAllBillingPlans();

        assertFalse(response.isEmpty());
        // assertEquals(12, response.get(0).getBillingDay());
        // assertEquals(12, response.get(0).getPaymentDueDays());
        assertEquals(0.1, response.get(0).getTaxRate());
        assertEquals(CREDIT_CARD, response.get(0).getPaymentMethod());
        assertEquals(contract.getId(), response.get(0).getContractId());
        assertEquals("Test Name", response.get(0).getName());
        assertEquals("Test Description", response.get(0).getDescription());
        assertEquals(MONTHLY, response.get(0).getCycleType());
        assertEquals(1000, response.get(0).getBaseAmount());

        verify(billingPlanService, times(1)).getAll();
    }

    @Test
    void deleteBillingPlan() {
        id = UUID.randomUUID();
        DeleteBillingPlanDto billingPlanDto = new DeleteBillingPlanDto();
        billingPlanDto.setId(id);
        doNothing().when(billingPlanService).delete(any(DeleteBillingPlanDto.class));
        billingPlanController.deleteBillingPlan(billingPlanDto);
        verify(billingPlanService, times(1)).delete(any(DeleteBillingPlanDto.class));
    }

    @Test
    void updateBillingPlan() {
        id = UUID.randomUUID();
        UpdateBillingPlanDto updateBillingPlanDto = new UpdateBillingPlanDto();
        updateBillingPlanDto.setId(id);
        updateBillingPlanDto.setName("Updated Name");
        updateBillingPlanDto.setDescription("Updated Description");
        updateBillingPlanDto.setBaseAmount(1200);
        updateBillingPlanDto.setCycleType(MONTHLY);
        updateBillingPlanDto.setBillingDay(15);
        updateBillingPlanDto.setPaymentDueDays(10);
        updateBillingPlanDto.setTaxRate(0.15);
        updateBillingPlanDto.setPaymentMethod(CREDIT_CARD);
        updateBillingPlanDto.setContractId(UUID.randomUUID());

        // Mock service response
        BillingPlan updatedBillingPlan = new BillingPlan();
        updatedBillingPlan.setId(id);
        updatedBillingPlan.setName("Updated Name");
        updatedBillingPlan.setDescription("Updated Description");
        updatedBillingPlan.setBaseAmount(1200);
        updatedBillingPlan.setCycleType(MONTHLY);
        // updatedBillingPlan.setBillingDay(15);
        // updatedBillingPlan.setPaymentDueDays(10);
        updatedBillingPlan.setTaxRate(0.15);
        updatedBillingPlan.setPaymentMethod(CREDIT_CARD);

        when(billingPlanService.update(any(UpdateBillingPlanDto.class))).thenReturn(updatedBillingPlan);

        billingPlanController.updateBillingPlan(updateBillingPlanDto);

        verify(billingPlanService, times(1)).update(any(UpdateBillingPlanDto.class));
    }

    @Test
    void updateBillingPlanNotFound() {
        id = UUID.randomUUID();
        UpdateBillingPlanDto updateBillingPlanDto = new UpdateBillingPlanDto();
        updateBillingPlanDto.setId(id);

        // Simulate the service throwing an exception
        when(billingPlanService.update(any(UpdateBillingPlanDto.class)))
                .thenThrow(new BusinessException("Billing Plan not found with id: " + id));

        // Act and check the exception
        assertThrows(BusinessException.class, () -> {
            billingPlanController.updateBillingPlan(updateBillingPlanDto);
        });
    }
}
