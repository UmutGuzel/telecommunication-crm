package com.gygy.contractservice.controller;

import com.gygy.contractservice.core.exception.type.BusinessException;
import com.gygy.contractservice.dto.contractDetail.ContractDetailListiningDto;
import com.gygy.contractservice.dto.contractDetail.CreateContractDetailDto;
import com.gygy.contractservice.dto.contractDetail.DeleteContractDetailDto;
import com.gygy.contractservice.dto.contractDetail.UpdateContractDetailDto;
import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.model.enums.Status;
import com.gygy.contractservice.service.ContractDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.gygy.contractservice.model.enums.ContractDetailType.CORPORATE;
import static com.gygy.contractservice.model.enums.ServiceType.INTERNET;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ContractDetailControllerTest {
    @Mock
    private ContractDetailService contractDetailService;
    @InjectMocks
    private ContractDetailController contractDetailController;

    private UUID id;
    private UUID contract_id;
    private UUID customer_id;
    private Contract contract;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addContractDetail() {
        CreateContractDetailDto createContractDetailDto = new CreateContractDetailDto();
        // doNothing().when(contractDetailService).add(any(CreateContractDetailDto.class));

        // contractDetailController.add(createContractDetailDto);
        // verify(contractDetailService,times(1)).add(any(CreateContractDetailDto.class));

    }

    @Test
    void getcontractDetail() {
        customer_id = UUID.randomUUID();
        contract_id = UUID.randomUUID();
        ContractDetailListiningDto contractDetailListiningDto = new ContractDetailListiningDto();
        contractDetailListiningDto.setContractId(contract_id);
        contractDetailListiningDto.setStatus(Status.ACTIVE);
        contractDetailListiningDto.setEndDate(LocalDate.now());
        contractDetailListiningDto.setContractDetailType(CORPORATE);
        contractDetailListiningDto.setCustomerId(customer_id);
        contractDetailListiningDto.setServiceType(INTERNET);
        contractDetailListiningDto.setStartDate(LocalDate.now());

        List<ContractDetailListiningDto> contractDetailListiningDtos = List.of(contractDetailListiningDto);
        when(contractDetailService.getAll()).thenReturn(contractDetailListiningDtos);

        List<ContractDetailListiningDto> response = contractDetailController.getAll();
        assertFalse(response.isEmpty());
        assertEquals(Status.ACTIVE, response.get(0).getStatus());
        assertEquals(contract_id, response.get(0).getContractId());
        assertEquals(customer_id, response.get(0).getCustomerId());
        assertEquals(LocalDate.now(), response.get(0).getStartDate());
        assertEquals(LocalDate.now(), response.get(0).getEndDate());
        assertEquals(CORPORATE, response.get(0).getContractDetailType());
        assertEquals(INTERNET, response.get(0).getServiceType());
        verify(contractDetailService, times(1)).getAll();
    }

    @Test
    void deleteContractDetail() {
        id = UUID.randomUUID();
        DeleteContractDetailDto deleteContractDetailDto = new DeleteContractDetailDto();
        deleteContractDetailDto.setId(id);
        doNothing().when(contractDetailService).delete(any(DeleteContractDetailDto.class));
        contractDetailController.delete(deleteContractDetailDto);
        verify(contractDetailService, times(1)).delete(any(DeleteContractDetailDto.class));
    }

    @Test
    void updateContractDetail() {
        id = UUID.randomUUID();
        contract_id = UUID.randomUUID();
        UpdateContractDetailDto updateContractDetailDto = new UpdateContractDetailDto();
        updateContractDetailDto.setContractId(contract_id);
        updateContractDetailDto.setStatus(Status.ACTIVE);
        updateContractDetailDto.setEndDate(LocalDate.now());
        updateContractDetailDto.setStartDate(LocalDate.now());
        updateContractDetailDto.setServiceType(INTERNET);

        ContractDetail updatedContractDetail = new ContractDetail();
        updatedContractDetail.setId(id);
        updatedContractDetail.setContract(contract);
        updatedContractDetail.setStatus(Status.ACTIVE);
        updatedContractDetail.setEndDate(LocalDate.now());
        updatedContractDetail.setStartDate(LocalDate.now());
        updatedContractDetail.setServiceType(INTERNET);
        when(contractDetailService.update(any(UpdateContractDetailDto.class))).thenReturn(updatedContractDetail);

        contractDetailController.update(updateContractDetailDto);

        verify(contractDetailService, times(1)).update(any(UpdateContractDetailDto.class));

    }

    @Test
    void updateContractDetailNotFound() {
        id = UUID.randomUUID();
        UpdateContractDetailDto updateContractDetailDto = new UpdateContractDetailDto();
        updateContractDetailDto.setId(id);

        // Simulate the service throwing an exception
        when(contractDetailService.update(any(UpdateContractDetailDto.class)))
                .thenThrow(new BusinessException("Contract Detail not found with id: " + id));

        // Act and check the exception
        assertThrows(BusinessException.class, () -> {
            contractDetailController.update(updateContractDetailDto);
        });
    }

}
