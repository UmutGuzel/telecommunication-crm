package com.gygy.contractservice.controller;

import com.gygy.contractservice.core.exception.type.BusinessException;
import com.gygy.contractservice.dto.contract.ContractListiningDto;
import com.gygy.contractservice.dto.contract.CreateContractDto;
import com.gygy.contractservice.dto.contract.DeleteContractDto;
import com.gygy.contractservice.dto.contract.UpdateContractDto;
import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.model.enums.Status;
import com.gygy.contractservice.service.ContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


 class ContractControllerTest {
    @Mock
    private ContractService contractService;
    @InjectMocks
    private ContractController contractController;

    private UUID id;
    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void addContract(){
        CreateContractDto createContractDto=new CreateContractDto();
        doNothing().when(contractService).add(any(CreateContractDto.class));

        contractController.add(createContractDto);
        verify(contractService,times(1)).add(any(CreateContractDto.class));
    }
    @Test
    void getContract(){
        ContractListiningDto contractListiningDto=new ContractListiningDto();
        contractListiningDto.setStatus(Status.ACTIVE);
        contractListiningDto.setContractNumber("123");
        contractListiningDto.setCreatedAt(LocalDateTime.now());
        contractListiningDto.setUpdatedAt(LocalDateTime.now());
        contractListiningDto.setDocumentUrl("Test Url");
        contractListiningDto.setSignatureDate(LocalDateTime.now());
        contractListiningDto.setDocumentType("Test Type");
        contractListiningDto.setUploadDate(LocalDateTime.now());

        List<ContractListiningDto> contractListiningDtos=List.of(contractListiningDto);
        when(contractService.getAll()).thenReturn(contractListiningDtos);
        List<ContractListiningDto> response =contractController.getAll();

        assertFalse(response.isEmpty());
        assertEquals("123",response.get(0).getContractNumber());
        assertEquals(Status.ACTIVE,response.get(0).getStatus());
        assertEquals(LocalDateTime.now().toLocalDate(),response.get(0).getCreatedAt().toLocalDate());
        assertEquals(LocalDateTime.now().toLocalDate(),response.get(0).getUpdatedAt().toLocalDate());
        assertEquals(LocalDateTime.now().toLocalDate(),response.get(0).getSignatureDate().toLocalDate());
        assertEquals(LocalDateTime.now().toLocalDate(),response.get(0).getUploadDate().toLocalDate());
        assertEquals("Test Url",response.get(0).getDocumentUrl());
        assertEquals("Test Type",response.get(0).getDocumentType());

        verify(contractService,times(1)).getAll();
    }
    @Test
    void deleteContract() {
        id = UUID.randomUUID();
        DeleteContractDto deleteContractDto= new DeleteContractDto();
        deleteContractDto.setId(id);
        doNothing().when(contractService).delete(any(DeleteContractDto.class));
        contractController.delete(deleteContractDto);
        verify(contractService,times(1)).delete(any(DeleteContractDto.class));
    }

    @Test
    void updateContract() {
        id = UUID.randomUUID();
        UpdateContractDto updateContractDto = new UpdateContractDto();
        updateContractDto.setId(id);
        updateContractDto.setStatus(Status.ACTIVE);
        updateContractDto.setContractNumber("23");
        updateContractDto.setDocumentType("Test Type");
        updateContractDto.setUploadDate(LocalDateTime.now());
        updateContractDto.setSignatureDate(LocalDateTime.now());

        // Mock service response
        Contract updatedContract = new Contract();
        updatedContract.setId(id);
        updatedContract.setStatus(Status.ACTIVE);
        updatedContract.setContractNumber("23");
        updatedContract.setDocumentType("Test Type");
        updatedContract.setUploadDate(LocalDateTime.now());
        updatedContract.setSignatureDate(LocalDateTime.now());

        when(contractService.update(any(UpdateContractDto.class))).thenReturn(updatedContract);

        contractController.update(updateContractDto);

        verify(contractService, times(1)).update(any(UpdateContractDto.class));
    }

    @Test
    void updateContractNotFound() {
        id = UUID.randomUUID();
        UpdateContractDto updateContractDto = new UpdateContractDto();
        updateContractDto.setId(id);

        // Simulate the service throwing an exception
            when(contractService.update(any(UpdateContractDto.class)))
                .thenThrow(new BusinessException("Contract not found with id: " + id));

        // Act and check the exception
        assertThrows(BusinessException.class, () -> {
            contractController.update(updateContractDto);
        });
        }


}
