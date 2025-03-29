package com.gygy.contractservice.service;

import com.gygy.contractservice.dto.contract.CreateContractDto;
import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.repository.ContractRepository;
import com.gygy.contractservice.service.impl.ContractServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.gygy.contractservice.model.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceImplTest {
    @Mock
    private ContractRepository contractRepository;
    @InjectMocks
    private ContractServiceImpl contractService;

    private UUID id;
    private Contract contract;
    private CreateContractDto createContractDto;

    @BeforeEach
    void setup(){
        id=UUID.randomUUID();
        contract=new Contract();
        contract.setId(id);
        contract.setSignatureDate(LocalDateTime.now());
        contract.setContractNumber("1212");
        contract.setDocumentUrl("Test URL");
        contract.setStatus(ACTIVE);
        contract.setCreatedAt(LocalDateTime.now());
        contract.setUploadDate(LocalDateTime.now());
        contract.setDocumentType("Test Type");

        createContractDto=new CreateContractDto();
        createContractDto.setStatus(ACTIVE);
        createContractDto.setDocumentType("Test URL");
        createContractDto.setUploadDate(LocalDateTime.now());
        createContractDto.setDocumentType("Test Type");
        createContractDto.setSignatureDate(LocalDateTime.now());
        createContractDto.setContractNumber("1212");
        createContractDto.setCreateDate(LocalDateTime.now());
    }
    @Test
    void whenAddCalledWithValidRequest_itShouldSaveContractToRepository(){
        //Arrange
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);

        //Act
        contractService.add(createContractDto);

        // Assert - Beklenen sonuçları doğruluyoruz
        verify(contractRepository, times(1)).save(any(Contract.class));
    }
    @Test
    void whenFindByIdCalledWithValidId_itShouldReturnContract() {
        // Arrange
        when(contractRepository.findById(id)).thenReturn(Optional.of(contract));

        // Act
        Optional<Contract> result = contractRepository.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(contract, result.get());
        verify(contractRepository, times(1)).findById(id);
    }




}
