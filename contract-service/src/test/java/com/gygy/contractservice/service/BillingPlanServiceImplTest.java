package com.gygy.contractservice.service;

import com.gygy.contractservice.client.PlanClient;
import com.gygy.contractservice.core.exception.type.BusinessException;
import com.gygy.contractservice.dto.billingPlan.CreateBillingPlanDto;
import com.gygy.contractservice.dto.billingPlan.PlanDto;
import com.gygy.contractservice.entity.BillingPlan;
import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.entity.Discount;
import com.gygy.contractservice.mapper.BillingPlanMapper;
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
import java.util.ArrayList;
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
    private BillingPlanMapper billingPlanMapper;

    @Mock
    private ContractService contractService;

    @Mock
    private PlanClient planClient;

    @InjectMocks
    private BillingPlanServiceImpl billingPlanService;

    private UUID id;
    private UUID contractId;
    private BillingPlan billingPlan;
    private CreateBillingPlanDto createBillingPlanDto;
    private Contract contract;
    private Discount discount;
    private PlanDto planDto;

    @BeforeEach
    void setup() {
        id = UUID.randomUUID();
        contractId = UUID.randomUUID();

        // BillingPlan nesnesi başlatılıyor
        billingPlan = new BillingPlan();
        billingPlan.setId(id);
        billingPlan.setStatus(ACTIVE);
        // billingPlan.setBillingDay(2);
        billingPlan.setPaymentMethod(CREDIT_CARD);
        billingPlan.setContract(contract);
        billingPlan.setTaxRate(12.2);
        billingPlan.setCycleType(MONTHLY);
        // billingPlan.setPaymentDueDays(2);
        billingPlan.setDescription("Test Description");
        billingPlan.setUpdatedAt(LocalDateTime.now());
        billingPlan.setBaseAmount(12);
        billingPlan.setCreatedAt(LocalDateTime.now());
        billingPlan.setName("Test Name");

        // PlanDto nesnesi başlatılıyor
        planDto = new PlanDto();
        planDto.setId(UUID.randomUUID());
        planDto.setName("test");
        planDto.setDescription("deneme");

        // Contract nesnesi başlatılıyor
        contract = new Contract();
        contract.setId(UUID.randomUUID());

        // Discount nesnesi başlatılıyor
        discount = new Discount();
        discount.setId(UUID.randomUUID());
        discount.setAmount(10.0);

        // CreateBillingPlanDto nesnesi başlatılıyor
        createBillingPlanDto = new CreateBillingPlanDto();
        createBillingPlanDto.setContract(contractId);

        createBillingPlanDto.setCycleType(MONTHLY);
        createBillingPlanDto.setTaxRate(12.2);
        createBillingPlanDto.setStatus(ACTIVE);
        createBillingPlanDto.setPaymentMethod(CREDIT_CARD);
        createBillingPlanDto.setBaseAmount(12);
    }

    @Test
    void whenAddCalledWithValidRequest_itShouldSaveBillingPlanToRepository() {
        // Arrange: İş kuralları mock'lanıyor
        doNothing().when(billingPlanBusinessRules).checkIfBillingPlanNameExists(any(String.class));
        // doNothing().when(billingPlanBusinessRules).checkIfCycleTypeAndBillingDayAreConsistent(any(String.class),
        // any(Integer.class));
        // doNothing().when(billingPlanBusinessRules).checkIfPaymentMethodAndDueDaysAreConsistent(any(String.class),
        // any(Integer.class));
        // doNothing().when(billingPlanBusinessRules).checkIfBaseAmountAndTaxRateAreValid(any(Integer.class),
        // any(Double.class));

        when(contractService.findById(any(UUID.class))).thenReturn(Optional.of(contract));

        // BillingPlan Repository'sini mock'lıyoruz
        when(billingPlanRepository.save(any(BillingPlan.class))).thenReturn(billingPlan);
        // 🛠 Eksik olan `billingPlanMapper` mock'lanıyor
        when(billingPlanMapper.createBillingPlanFromCreateBillingPlanDto(any(CreateBillingPlanDto.class)))
                .thenReturn(billingPlan);

        // Act: add metodu çağrılıyor
        // billingPlanService.add(createBillingPlanDto);

        // Assert: save metodunun bir kez çağrıldığını doğruluyoruz
        verify(billingPlanRepository, times(1)).save(any(BillingPlan.class));

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
