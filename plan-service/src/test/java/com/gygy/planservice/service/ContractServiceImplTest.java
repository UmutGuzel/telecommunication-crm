package com.gygy.planservice.service;

import com.gygy.planservice.dto.ContractDto;
import com.gygy.planservice.dto.ContractRequestDto;
import com.gygy.planservice.dto.PlanDto;
import com.gygy.planservice.entity.Contract;
import com.gygy.planservice.entity.Plan;
import com.gygy.planservice.exception.ContractNotFoundException;
import com.gygy.planservice.exception.InvalidInputException;
import com.gygy.planservice.exception.PlanNotFoundException;
import com.gygy.planservice.mapper.ContractMapper;
import com.gygy.planservice.repository.ContractRepository;
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
class ContractServiceImplTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private ContractMapper contractMapper;

    @InjectMocks
    private ContractServiceImpl contractService;

    private UUID contractId;
    private UUID planId;
    private Contract contract;
    private Plan plan;
    private PlanDto planDto;
    private ContractDto contractDto;
    private ContractRequestDto requestDto;

    @BeforeEach
    void setUp() {
        contractId = UUID.randomUUID();
        planId = UUID.randomUUID();
        
        plan = new Plan();
        plan.setId(planId);
        plan.setName("Test Plan");
        plan.setDescription("Test Description");
        plan.setPrice(BigDecimal.valueOf(99.99));
        plan.setIsPassive(false);
        
        planDto = new PlanDto();
        planDto.setId(planId);
        planDto.setName("Test Plan");
        planDto.setDescription("Test Description");
        planDto.setPrice(BigDecimal.valueOf(99.99));
        
        contract = new Contract();
        contract.setId(contractId);
        contract.setType("MONTHLY");
        contract.setDiscount(BigDecimal.valueOf(10.0));
        contract.setPlan(plan);
        contract.setIsPassive(false);
        contract.setCreatedAt(LocalDateTime.now());
        contract.setUpdatedAt(LocalDateTime.now());
        
        contractDto = new ContractDto();
        contractDto.setId(contractId);
        contractDto.setType("MONTHLY");
        contractDto.setDiscount(BigDecimal.valueOf(10.0));
        contractDto.setPlan(planDto);
        
        requestDto = new ContractRequestDto();
        requestDto.setType("MONTHLY");
        requestDto.setDiscount(BigDecimal.valueOf(10.0));
        requestDto.setPlanId(planId);
    }

    @Test
    void createContract_ValidRequest_ReturnsContractDto() {
        // Arrange
        when(planRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(contractMapper.toEntity(requestDto, plan)).thenReturn(contract);
        when(contractRepository.save(contract)).thenReturn(contract);
        when(contractMapper.toDto(contract)).thenReturn(contractDto);

        // Act
        ContractDto result = contractService.createContract(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(contractDto.getId(), result.getId());
        assertEquals(contractDto.getType(), result.getType());
        assertEquals(contractDto.getDiscount(), result.getDiscount());
        assertEquals(contractDto.getPlan().getId(), result.getPlan().getId());
        
        verify(planRepository).findById(planId);
        verify(contractMapper).toEntity(requestDto, plan);
        verify(contractRepository).save(contract);
        verify(contractMapper).toDto(contract);
    }

    @Test
    void createContract_NullRequest_ThrowsInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> contractService.createContract(null));
        
        verify(planRepository, never()).findById(any());
        verify(contractMapper, never()).toEntity(any(), any());
        verify(contractRepository, never()).save(any());
    }

    @Test
    void createContract_NullPlanId_ThrowsInvalidInputException() {
        // Arrange
        requestDto.setPlanId(null);
        
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> contractService.createContract(requestDto));
        
        verify(planRepository, never()).findById(any());
        verify(contractMapper, never()).toEntity(any(), any());
        verify(contractRepository, never()).save(any());
    }

    @Test
    void createContract_NonExistingPlanId_ThrowsPlanNotFoundException() {
        // Arrange
        when(planRepository.findById(planId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlanNotFoundException.class, () -> contractService.createContract(requestDto));
        
        verify(planRepository).findById(planId);
        verify(contractMapper, never()).toEntity(any(), any());
        verify(contractRepository, never()).save(any());
    }

    @Test
    void getAllContracts_ReturnsAllContracts() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAll()).thenReturn(contracts);
        when(contractMapper.toDto(contract)).thenReturn(contractDto);

        // Act
        List<ContractDto> result = contractService.getAllContracts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(contractDto, result.get(0));
        
        verify(contractRepository).findAll();
        verify(contractMapper).toDto(contract);
    }

    @Test
    void getContractById_ExistingId_ReturnsContractDto() {
        // Arrange
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
        when(contractMapper.toDto(contract)).thenReturn(contractDto);

        // Act
        ContractDto result = contractService.getContractById(contractId);

        // Assert
        assertNotNull(result);
        assertEquals(contractDto.getId(), result.getId());
        assertEquals(contractDto.getType(), result.getType());
        assertEquals(contractDto.getDiscount(), result.getDiscount());
        assertEquals(contractDto.getPlan().getId(), result.getPlan().getId());
        
        verify(contractRepository).findById(contractId);
        verify(contractMapper).toDto(contract);
    }

    @Test
    void getContractById_NullId_ThrowsInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> contractService.getContractById(null));
        
        verify(contractRepository, never()).findById(any());
    }

    @Test
    void getContractById_NonExistingId_ThrowsContractNotFoundException() {
        // Arrange
        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ContractNotFoundException.class, () -> contractService.getContractById(contractId));
        
        verify(contractRepository).findById(contractId);
        verify(contractMapper, never()).toDto(any());
    }

    @Test
    void updateContract_ValidRequest_ReturnsContractDto() {
        // Arrange
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
        when(planRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(contractRepository.save(contract)).thenReturn(contract);
        when(contractMapper.toDto(contract)).thenReturn(contractDto);

        // Act
        ContractDto result = contractService.updateContract(contractId, requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(contractDto.getId(), result.getId());
        assertEquals(contractDto.getType(), result.getType());
        assertEquals(contractDto.getDiscount(), result.getDiscount());
        assertEquals(contractDto.getPlan().getId(), result.getPlan().getId());
        
        verify(contractRepository).findById(contractId);
        verify(planRepository).findById(planId);
        verify(contractRepository).save(contract);
        verify(contractMapper).toDto(contract);
    }

    @Test
    void updateContract_NullId_ThrowsInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> contractService.updateContract(null, requestDto));
        
        verify(contractRepository, never()).findById(any());
        verify(planRepository, never()).findById(any());
        verify(contractRepository, never()).save(any());
    }

    @Test
    void updateContract_NullRequest_ThrowsInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> contractService.updateContract(contractId, null));
        
        verify(contractRepository, never()).findById(any());
        verify(planRepository, never()).findById(any());
        verify(contractRepository, never()).save(any());
    }

    @Test
    void updateContract_NullPlanId_ThrowsInvalidInputException() {
        // Arrange
        requestDto.setPlanId(null);
        
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> contractService.updateContract(contractId, requestDto));
        
        verify(contractRepository, never()).findById(any());
        verify(planRepository, never()).findById(any());
        verify(contractRepository, never()).save(any());
    }

    @Test
    void updateContract_NonExistingContractId_ThrowsContractNotFoundException() {
        // Arrange
        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ContractNotFoundException.class, () -> contractService.updateContract(contractId, requestDto));
        
        verify(contractRepository).findById(contractId);
        verify(planRepository, never()).findById(any());
        verify(contractRepository, never()).save(any());
    }

    @Test
    void updateContract_NonExistingPlanId_ThrowsPlanNotFoundException() {
        // Arrange
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
        when(planRepository.findById(planId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlanNotFoundException.class, () -> contractService.updateContract(contractId, requestDto));
        
        verify(contractRepository).findById(contractId);
        verify(planRepository).findById(planId);
        verify(contractRepository, never()).save(any());
    }

    @Test
    void deleteContract_ExistingId_SetsIsPassiveTrue() {
        // Arrange
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
        when(contractRepository.save(contract)).thenReturn(contract);

        // Act
        contractService.deleteContract(contractId);

        // Assert
        assertTrue(contract.getIsPassive());
        
        verify(contractRepository).findById(contractId);
        verify(contractRepository).save(contract);
    }

    @Test
    void deleteContract_NullId_ThrowsInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> contractService.deleteContract(null));
        
        verify(contractRepository, never()).findById(any());
        verify(contractRepository, never()).save(any());
    }

    @Test
    void deleteContract_NonExistingId_ThrowsContractNotFoundException() {
        // Arrange
        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ContractNotFoundException.class, () -> contractService.deleteContract(contractId));
        
        verify(contractRepository).findById(contractId);
        verify(contractRepository, never()).save(any());
    }

    @Test
    void getAllActiveContracts_ReturnsActiveContracts() {
        // Arrange
        List<Contract> activeContracts = Arrays.asList(contract);
        when(contractRepository.findAllByIsPassiveFalse()).thenReturn(activeContracts);
        when(contractMapper.toDto(contract)).thenReturn(contractDto);

        // Act
        List<ContractDto> result = contractService.getAllActiveContracts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(contractDto, result.get(0));
        
        verify(contractRepository).findAllByIsPassiveFalse();
        verify(contractMapper).toDto(contract);
    }
} 