package com.gygy.contractservice.service;

import com.gygy.contractservice.dto.billingPlan.CreateBillingPlanDto;
import com.gygy.contractservice.dto.commitment.CreateCommitmentDto;
import com.gygy.contractservice.entity.Commitment;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.mapper.CommitmentMapper;
import com.gygy.contractservice.repository.CommitmentRepository;
import com.gygy.contractservice.rules.CommitmentBusinessRules;
import com.gygy.contractservice.service.impl.CommitmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.gygy.contractservice.model.enums.BillingCycleType.MONTHLY;
import static com.gygy.contractservice.model.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class CommitmentServiceImplTest {
    @Mock
    private CommitmentRepository commitmentRepository;

    @Mock
    private ContractDetailService contractDetailService;
    @Mock
    private CommitmentMapper commitmentMapper;

    @Mock
    private CommitmentBusinessRules commitmentBusinessRules;

    @InjectMocks
    private CommitmentServiceImpl commitmentService;

    private UUID id;
    private UUID contractDetailId;
    private Commitment commitment;
    private CreateCommitmentDto createCommitmentDto;
    private ContractDetail contractDetail;

    @BeforeEach
    void setup() {
        id = UUID.randomUUID();
        contractDetailId = UUID.randomUUID();

        commitment = new Commitment();
        commitment.setId(id);
        commitment.setStatus(ACTIVE);
        commitment.setUpdatedAt(LocalDateTime.now());
        commitment.setEarlyTerminationFee(123.45);
        commitment.setEndDate(LocalDate.now());
        commitment.setStartDate(LocalDate.now());
        commitment.setCycleType(MONTHLY);
        commitment.setName("Test Name");
        commitment.setDurationMonths(2);
        commitment.setBillingDay(2);

        // ContractDetail nesnesi başlatılıyor
        contractDetail = new ContractDetail();
        contractDetail.setId(UUID.randomUUID()); // Customer ID atanıyor

        // CreateCommitmentDto nesnesi başlatılıyor
        createCommitmentDto = new CreateCommitmentDto();
        createCommitmentDto.setName("Test Name");
        createCommitmentDto.setStatus(ACTIVE);
        createCommitmentDto.setCycleType(MONTHLY);
        createCommitmentDto.setEndDate(LocalDate.now());
        createCommitmentDto.setStartDate(LocalDate.now());
        createCommitmentDto.setDurationMonths(2);
        createCommitmentDto.setEarlyTerminationFee(123.45);
        createCommitmentDto.setContractDetailId(contractDetailId);
        createCommitmentDto.setBillingDay(2);
    }

    @Test
    void whenAddCalledWithValidRequest_itShouldSaveCommitmentToRepository() {
        // Arrange: İş kuralları mock'lanıyor (doNothing ile void dönen metodlar mock'lanır)
        doNothing().when(commitmentBusinessRules).checkIfCommitmentNameExists(any(String.class));
        doNothing().when(commitmentBusinessRules).checkIfCycleTypeAndBillingDayAreConsistent(any(String.class), any(Integer.class));
        doNothing().when(commitmentBusinessRules).checkIfCommitmentDatesAreValid(any(LocalDate.class), any(LocalDate.class));
        doNothing().when(commitmentBusinessRules).checkIfCustomerCanMakeNewCommitment(any());

        // ContractDetail'ı mock'lıyoruz
        when(contractDetailService.findById(contractDetailId)).thenReturn(contractDetail);

        when(commitmentMapper.createCommitmentFromCreateCommitmentDto(any(CreateCommitmentDto.class)))
                .thenReturn(commitment);

        // Commitment Repository'sini mock'lıyoruz
        when(commitmentRepository.save(any(Commitment.class))).thenReturn(commitment);

        // Act: add metodu çağrılıyor
        commitmentService.add(createCommitmentDto);

        // Assert: save metodunun bir kez çağrıldığını doğruluyoruz
        verify(commitmentRepository, times(1)).save(any(Commitment.class));

        // İş kurallarının da çağrıldığını doğruluyoruz
        verify(commitmentBusinessRules, times(1)).checkIfCommitmentNameExists(any(String.class));
        verify(commitmentBusinessRules, times(1)).checkIfCycleTypeAndBillingDayAreConsistent(any(String.class), any(Integer.class));
        verify(commitmentBusinessRules, times(1)).checkIfCommitmentDatesAreValid(any(LocalDate.class), any(LocalDate.class));
        verify(commitmentBusinessRules, times(1)).checkIfCustomerCanMakeNewCommitment(any());
    }

    @Test
    void whenAddCalledWithInvalidRequest_itShouldThrowException() {
        // Arrange: checkIfCommitmentNameExists metodu false döndürüyor ve bu durumda exception fırlatılacak
        doThrow(new IllegalArgumentException("Commitment name already exists"))
                .when(commitmentBusinessRules).checkIfCommitmentNameExists(any(String.class));

        // Act & Assert: Geçersiz durumda exception bekliyoruz
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> commitmentService.add(createCommitmentDto));

        // Assert: Exception mesajını doğruluyoruz
        assertEquals("Commitment name already exists", exception.getMessage());
    }

    @Test
    void whenFindByIdCalledWithValidId_itShouldReturnCommitment() {
        // Arrange
        when(commitmentRepository.findById(id)).thenReturn(Optional.of(commitment));

        // Act
        Optional<Commitment> result = commitmentRepository.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(commitment, result.get());
        verify(commitmentRepository, times(1)).findById(id);
    }



}
