package com.gygy.contractservice.controller;

import com.gygy.contractservice.core.exception.type.BusinessException;
import com.gygy.contractservice.dto.discount.CreateDiscountDto;
import com.gygy.contractservice.dto.discount.DeleteDiscountDto;
import com.gygy.contractservice.dto.discount.DiscountListiningDto;
import com.gygy.contractservice.dto.discount.UpdateDiscountDto;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.entity.Discount;
import com.gygy.contractservice.model.enums.Status;
import com.gygy.contractservice.service.DiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.gygy.contractservice.model.enums.DiscountType.STUDENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

class DiscountControllerTest {
    @Mock
    private DiscountService discountService;
    @InjectMocks
    private DiscountController discountController;
    private UUID id;
    private UUID billingPlanId;
    private UUID contractDetailId;
    private UUID customerId;
    private ContractDetail contractDetail;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addDiscount() {
        CreateDiscountDto createDiscountDto = new CreateDiscountDto();
        doNothing().when(discountService).add(any(CreateDiscountDto.class));
        discountController.add(createDiscountDto);
        verify(discountService, times(1)).add(any(CreateDiscountDto.class));
    }

    @Test
    void getDiscount() {
        billingPlanId = UUID.randomUUID();
        contractDetailId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        DiscountListiningDto discountListiningDto = new DiscountListiningDto();
        discountListiningDto.setAmount(100.0);
        discountListiningDto.setDiscountType(STUDENT);
        discountListiningDto.setDescription("Test Description");
        discountListiningDto.setStatus(Status.ACTIVE);
        discountListiningDto.setEndDate(LocalDate.now());
        discountListiningDto.setStartDate(LocalDate.now());
        discountListiningDto.setBillingPlanId(Collections.singletonList(billingPlanId));
        // discountListiningDto.setContractDetailId(contractDetailId);
        discountListiningDto.setCustomerId(customerId);
        discountListiningDto.setPercentage(0.1);
        discountListiningDto.setCreateDate(LocalDate.now());
        discountListiningDto.setUpdateDate(LocalDate.now());

        List<DiscountListiningDto> discountListiningDtos = List.of(discountListiningDto);
        when(discountService.getAll()).thenReturn(discountListiningDtos);
        List<DiscountListiningDto> response = discountController.getAll();

        assertFalse(response.isEmpty());
        assertEquals(100.0, response.get(0).getAmount());
        assertEquals(STUDENT, response.get(0).getDiscountType());
        assertEquals("Test Description", response.get(0).getDescription());
        assertEquals(Status.ACTIVE, response.get(0).getStatus());
        assertEquals(LocalDate.now(), response.get(0).getEndDate());
        assertEquals(LocalDate.now(), response.get(0).getStartDate());
        assertEquals(billingPlanId, response.get(0).getBillingPlanId().get(0)); // Düzeltildi!
        // assertEquals(contractDetailId,response.get(0).getContractDetailId());
        assertEquals(customerId, response.get(0).getCustomerId());
        assertEquals(0.1, response.get(0).getPercentage());
        assertEquals(LocalDate.now(), response.get(0).getCreateDate());
        assertEquals(LocalDate.now(), response.get(0).getUpdateDate());

        verify(discountService, times(1)).getAll();
    }

    @Test
    void deleteContract() {
        id = UUID.randomUUID();
        DeleteDiscountDto deleteDiscountDto = new DeleteDiscountDto();
        deleteDiscountDto.setId(id);
        doNothing().when(discountService).delete(any(DeleteDiscountDto.class));
        discountController.delete(deleteDiscountDto);
        verify(discountService, times(1)).delete(any(DeleteDiscountDto.class));
    }

    @Test
    void updateDiscount() {
        id = UUID.randomUUID();
        customerId = UUID.randomUUID();
        billingPlanId = UUID.randomUUID();
        contractDetailId = UUID.randomUUID();
        UpdateDiscountDto updateDiscountDto = new UpdateDiscountDto();
        updateDiscountDto.setId(id);
        updateDiscountDto.setDiscountType(STUDENT);
        updateDiscountDto.setAmount(100);
        updateDiscountDto.setCustomerId(customerId);
        updateDiscountDto.setPercentage(0.1);
        updateDiscountDto.setBillingPlanId(Collections.singletonList(billingPlanId));
        updateDiscountDto.setStatus(Status.ACTIVE);
        updateDiscountDto.setStartDate(LocalDate.now());
        updateDiscountDto.setEndDate(LocalDate.now());
        updateDiscountDto.setDescription("Test Description");
        // updateDiscountDto.setContractDetailId(contractDetailId);

        Discount updatedDiscount = new Discount();
        updatedDiscount.setId(id);
        updatedDiscount.setDiscountType(STUDENT);
        updatedDiscount.setAmount(100);
        updatedDiscount.setCustomerId(customerId);
        updatedDiscount.setPercentage(0.1);
        updatedDiscount.setStatus(Status.ACTIVE);
        updatedDiscount.setStartDate(LocalDate.now());
        updatedDiscount.setEndDate(LocalDate.now());
        updatedDiscount.setDescription("Test Description");
        // updatedDiscount.setContractDetail(contractDetail);
        when(discountService.update(any(UpdateDiscountDto.class))).thenReturn(updatedDiscount);

        discountController.update(updateDiscountDto);

        verify(discountService, times(1)).update(any(UpdateDiscountDto.class));
    }

    @Test
    void updateDiscountNotFound() {
        id = UUID.randomUUID();
        UpdateDiscountDto updateDiscountDto = new UpdateDiscountDto();
        updateDiscountDto.setId(id);

        // Simulate the service throwing an exception
        when(discountService.update(any(UpdateDiscountDto.class)))
                .thenThrow(new BusinessException("Discount not found with id: " + id));

        // Act and check the exception
        assertThrows(BusinessException.class, () -> {
            discountController.update(updateDiscountDto);
        });
    }

}
