package com.gygy.contractservice.service;

import com.gygy.contractservice.core.exception.type.BusinessException;
import com.gygy.contractservice.dto.billingPlan.CreateBillingPlanDto;
import com.gygy.contractservice.entity.BillingPlan;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.entity.Discount;
import com.gygy.contractservice.repository.BillingPlanRepository;
import com.gygy.contractservice.rules.BillingPlanBusinessRules;
import com.gygy.contractservice.service.impl.BillingPlanServiceImpl;
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

import static com.gygy.contractservice.model.enums.BillingCycleType.MONTHLY;
import static com.gygy.contractservice.model.enums.PaymentMethod.CREDIT_CARD;
import static com.gygy.contractservice.model.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class BillingPlanServiceImplTest {

    @Mock
    private BillingPlanRepository billingPlanRepository;

    @Mock
    private BillingPlanBusinessRules billingPlanBusinessRules;

    @Mock
    private DiscountService discountService;

    @Mock
    private ContractDetailService contractDetailService; // Mock olarak eklendi

    @InjectMocks
    private BillingPlanServiceImpl billingPlanService;

    private UUID id;
    private UUID contractDetailId;
    private BillingPlan billingPlan;
    private CreateBillingPlanDto createBillingPlanDto;
    private ContractDetail contractDetail;
    private Discount discount;

    @BeforeEach
    void setup() {
        id = UUID.randomUUID();
        contractDetailId = UUID.randomUUID();

        // BillingPlan nesnesi başlatılıyor
        billingPlan = new BillingPlan();
        billingPlan.setId(id);
        billingPlan.setStatus(ACTIVE);
        billingPlan.setBillingDay(2);
        billingPlan.setPaymentMethod(CREDIT_CARD);
        billingPlan.setContractDetail(contractDetail);
        billingPlan.setTaxRate(12.2);
        billingPlan.setCycleType(MONTHLY);
        billingPlan.setPaymentDueDays(2);
        billingPlan.setDescription("Test Description");
        billingPlan.setUpdatedAt(LocalDateTime.now());
        billingPlan.setBaseAmount(12);
        billingPlan.setCreatedAt(LocalDateTime.now());
        billingPlan.setName("Test Name");

        // ContractDetail nesnesi başlatılıyor
        contractDetail = new ContractDetail();
        contractDetail.setId(UUID.randomUUID());

        // Discount nesnesi başlatılıyor
        discount = new Discount();
        discount.setId(UUID.randomUUID());
        discount.setAmount(10.0);

        createBillingPlanDto = new CreateBillingPlanDto();
        createBillingPlanDto.setContractDetailId(contractDetailId);
        createBillingPlanDto.setName("Test Name");
        createBillingPlanDto.setCycleType(MONTHLY);
        createBillingPlanDto.setTaxRate(12.2);
        createBillingPlanDto.setStatus(ACTIVE);
        createBillingPlanDto.setPaymentMethod(CREDIT_CARD);
        createBillingPlanDto.setBillingDay(2);
        createBillingPlanDto.setPaymentDueDays(2);
        createBillingPlanDto.setBaseAmount(12);
        createBillingPlanDto.setDiscountIds(Arrays.asList(discount.getId()));
    }

    @Test
    void whenAddCalledWithValidRequest_itShouldSaveBillingPlanToRepository() {
        // Arrange: İş kuralları mock'lanıyor
        doNothing().when(billingPlanBusinessRules).checkIfBillingPlanNameExists(any(String.class));
        doNothing().when(billingPlanBusinessRules).checkIfCycleTypeAndBillingDayAreConsistent(any(String.class), any(Integer.class));
        doNothing().when(billingPlanBusinessRules).checkIfPaymentMethodAndDueDaysAreConsistent(any(String.class), any(Integer.class));
        doNothing().when(billingPlanBusinessRules).checkIfBaseAmountAndTaxRateAreValid(any(Integer.class), any(Double.class));

        when(contractDetailService.findById(any(UUID.class))).thenReturn(contractDetail);

        // DiscountService'in findAllById metodunu mock'lıyoruz
        when(discountService.findAllById(any())).thenReturn(Arrays.asList(discount));

        // BillingPlan Repository'sini mock'lıyoruz
        when(billingPlanRepository.save(any(BillingPlan.class))).thenReturn(billingPlan);

        // Act: add metodu çağrılıyor
        billingPlanService.add(createBillingPlanDto);

        // Assert: save metodunun bir kez çağrıldığını doğruluyoruz
        verify(billingPlanRepository, times(1)).save(any(BillingPlan.class));

        // Assert: DiscountService'in findAllById metodunun çağrıldığını doğruluyoruz
        verify(discountService, times(1)).findAllById(any());
    }

    @Test
    void whenFindByIdCalledWithInvalidId_itShouldThrowException() {
        // Arrange: Bulunmayan bir BillingPlan ID'si ile findById metodunu mock'lıyoruz
        when(billingPlanRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert: Hata fırlatılmasını bekliyoruz
        assertThrows(BusinessException.class, () -> billingPlanService.findById(UUID.randomUUID()));
    }
    @Test
    void whenFindByIdCalledWithValidId_itShouldReturnCommitment() {
        // Arrange
        when(billingPlanRepository.findById(id)).thenReturn(Optional.of(billingPlan));

        // Act
        Optional<BillingPlan> result = billingPlanRepository.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(billingPlan, result.get());
        verify(billingPlanRepository, times(1)).findById(id);
    }

}
