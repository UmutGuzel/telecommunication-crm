package com.gygy.planservice.controller;

import com.gygy.planservice.dto.ContractDto;
import com.gygy.planservice.dto.ContractRequestDto;
import com.gygy.planservice.service.ContractService;
import com.gygy.planservice.exception.ContractNotFoundException;
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

class ContractControllerTest {

    @Mock
    private ContractService contractService;

    @InjectMocks
    private ContractController contractController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createContract() {
        ContractRequestDto requestDto = new ContractRequestDto();
        ContractDto contractDto = new ContractDto();
        when(contractService.createContract(any(ContractRequestDto.class))).thenReturn(contractDto);

        ResponseEntity<ContractDto> response = contractController.createContract(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(contractDto, response.getBody());
        verify(contractService).createContract(requestDto);
    }

    @Test
    void getContractById() {
        UUID id = UUID.randomUUID();
        ContractDto contractDto = new ContractDto();
        when(contractService.getContractById(id)).thenReturn(contractDto);

        ResponseEntity<ContractDto> response = contractController.getContractById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contractDto, response.getBody());
        verify(contractService).getContractById(id);
    }

    @Test
    void getContractById_NotFound() {
        UUID id = UUID.randomUUID();
        when(contractService.getContractById(id)).thenThrow(new ContractNotFoundException("Contract not found"));

        ContractNotFoundException exception = assertThrows(ContractNotFoundException.class, () -> {
            contractController.getContractById(id);
        });

        assertEquals("Contract not found", exception.getMessage());
        verify(contractService).getContractById(id);
    }

    @Test
    void getAllContracts() {
        when(contractService.getAllContracts()).thenReturn(Collections.emptyList());

        ResponseEntity<List<ContractDto>> response = contractController.getAllContracts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(contractService).getAllContracts();
    }

    @Test
    void updateContract() {
        UUID id = UUID.randomUUID();
        ContractRequestDto requestDto = new ContractRequestDto();
        ContractDto contractDto = new ContractDto();
        when(contractService.updateContract(eq(id), any(ContractRequestDto.class))).thenReturn(contractDto);

        ResponseEntity<ContractDto> response = contractController.updateContract(id, requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contractDto, response.getBody());
        verify(contractService).updateContract(id, requestDto);
    }

    @Test
    void updateContract_NotFound() {
        UUID id = UUID.randomUUID();
        ContractRequestDto requestDto = new ContractRequestDto();
        when(contractService.updateContract(eq(id), any(ContractRequestDto.class)))
                .thenThrow(new ContractNotFoundException("Contract not found"));

        ContractNotFoundException exception = assertThrows(ContractNotFoundException.class, () -> {
            contractController.updateContract(id, requestDto);
        });

        assertEquals("Contract not found", exception.getMessage());
        verify(contractService).updateContract(id, requestDto);
    }

    @Test
    void deleteContract() {
        UUID id = UUID.randomUUID();
        doNothing().when(contractService).deleteContract(id);

        ResponseEntity<Void> response = contractController.deleteContract(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(contractService).deleteContract(id);
    }

    @Test
    void getAllActiveContracts() {
        when(contractService.getAllActiveContracts()).thenReturn(Collections.emptyList());

        ResponseEntity<List<ContractDto>> response = contractController.getAllActiveContracts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(contractService).getAllActiveContracts();
    }
} 