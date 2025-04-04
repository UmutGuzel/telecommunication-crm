package com.gygy.contractservice.service;

import com.gygy.contractservice.dto.discount.CreateDiscountDto;
import com.gygy.contractservice.entity.Discount;
import com.gygy.contractservice.mapper.DiscountMapper;
import com.gygy.contractservice.repository.DiscountRepository;
import com.gygy.contractservice.service.impl.DiscountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DiscountServiceImplTest {

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private BillingPlanService billingPlanService;

    @Mock
    private ContractDetailService contractDetailService;

    @Mock
    private DiscountMapper discountMapper;

    @InjectMocks
    private DiscountServiceImpl discountService;

    private UUID discountId;
    private CreateDiscountDto createDiscountDto;
    private Discount discount;

    @BeforeEach
    void setup() {
        discountId = UUID.randomUUID();
        discount = new Discount();
        discount.setId(discountId);
        discount.setStartDate(LocalDate.now());
        discount.setEndDate(LocalDate.now());
        discount.setPercentage(0.2);
        discount.setDescription("Test Description");
        discount.setAmount(12.22);

        createDiscountDto = new CreateDiscountDto();
        createDiscountDto.setDescription("Test Description");
        createDiscountDto.setAmount(12.22);
        createDiscountDto.setStartDate(LocalDate.now());
        createDiscountDto.setEndDate(LocalDate.now());
    }

    @Test
    void whenAddCalledWithValidRequest_itShouldSaveDiscountToRepository() {
        when(discountMapper.createDiscountFromCreateDiscountDto(createDiscountDto)).thenReturn(discount);
        when(discountRepository.save(any(Discount.class))).thenReturn(discount);

        // Act: Calling the add method
        discountService.add(createDiscountDto);

        // Assert: Verifying the interactions
        verify(discountRepository, times(1)).save(any(Discount.class));
        verify(discountMapper, times(1)).createDiscountFromCreateDiscountDto(createDiscountDto);
    }



    @Test
     void whenFindByIdCalledWithValidId_itShouldReturnDiscount() {
        // Arrange
        when(discountRepository.findById(discountId)).thenReturn(Optional.of(discount));

        // Act
        Optional<Discount> result = Optional.ofNullable(discountService.findById(discountId));

        // Assert
        assertNotNull(result);
        assertEquals(discount, result.get());
        verify(discountRepository, times(1)).findById(discountId);
    }

}
