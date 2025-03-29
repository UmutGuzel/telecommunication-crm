package com.gygy.contractservice.service;

import com.gygy.contractservice.dto.contractDetail.CreateContractDetailDto;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.repository.ContractDetailRepository;
import com.gygy.contractservice.service.impl.ContractDetailServiceImpl;
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

import static com.gygy.contractservice.model.enums.ContractDetailType.INDIVIDUAL;
import static com.gygy.contractservice.model.enums.ServiceType.INTERNET;
import static com.gygy.contractservice.model.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractDetailServiceImplTest {

    @Mock
    private ContractDetailRepository contractDetailRepository; // Sadece ContractDetailRepository mock'ı

    @InjectMocks
    private ContractDetailServiceImpl contractDetailServiceImpl;

    private UUID id;
    private UUID contract_id;
    private UUID customer_id;
    private ContractDetail contractDetail;
    private CreateContractDetailDto createContractDetailDto;

    @BeforeEach
    void setup() {
        id = UUID.randomUUID();
        customer_id = UUID.randomUUID();
        contract_id = UUID.randomUUID();

        contractDetail = new ContractDetail();
        contractDetail.setId(id);
        contractDetail.setStartDate(LocalDate.now());
        contractDetail.setEndDate(LocalDate.now());
        contractDetail.setCreatedAt(LocalDateTime.now());
        contractDetail.setCustomerId(customer_id);
        contractDetail.setServiceType(INTERNET);
        contractDetail.setContractDetailType(INDIVIDUAL);
        contractDetail.setStatus(ACTIVE);

        createContractDetailDto = new CreateContractDetailDto();
        createContractDetailDto.setContractId(contract_id); // Doğru contractId burada kullanılmalı
        createContractDetailDto.setStatus(ACTIVE);
        createContractDetailDto.setEndDate(LocalDate.now());
        createContractDetailDto.setStartDate(LocalDate.now());
        createContractDetailDto.setContractDetailType(INDIVIDUAL);
        createContractDetailDto.setServiceType(INTERNET);
        createContractDetailDto.setCustomerId(customer_id);
    }

    @Test
    void whenAddCalledWithValidRequest_itShouldSaveContractDetailToRepository() {
        // Arrange: save metodu mock'lanıyor
        when(contractDetailRepository.save(any(ContractDetail.class))).thenReturn(contractDetail);

        // Act: service metodu çağrılır
        contractDetailServiceImpl.add(createContractDetailDto);

        // Assert: Beklenen işlemler doğrulanır
        verify(contractDetailRepository, times(1)).save(any(ContractDetail.class));
    }
    @Test
    void whenFindByIdCalledWithValidId_itShouldReturnCommitment() {
        // Arrange
        when(contractDetailRepository.findById(id)).thenReturn(Optional.of(contractDetail));

        // Act
        Optional<ContractDetail> result = contractDetailRepository.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(contractDetail, result.get());
        verify(contractDetailRepository, times(1)).findById(id);
    }


}
