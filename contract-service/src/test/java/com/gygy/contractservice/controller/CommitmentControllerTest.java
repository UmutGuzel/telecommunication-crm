package com.gygy.contractservice.controller;

import com.gygy.contractservice.dto.commitment.CommitmentListiningDto;
import com.gygy.contractservice.dto.commitment.CreateCommitmentDto;
import com.gygy.contractservice.dto.commitment.DeleteCommitmentDto;
import com.gygy.contractservice.dto.commitment.UpdateCommitmentDto;
import com.gygy.contractservice.entity.Commitment;
import com.gygy.contractservice.model.enums.Status;
import com.gygy.contractservice.service.CommitmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.gygy.contractservice.model.enums.BillingCycleType.MONTHLY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommitmentControllerTest {
    @Mock
    private CommitmentService commitmentService;
    @InjectMocks
    private CommitmentController commitmentController;

    private UUID id;
    private UUID contract_id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

        @Test
        void addCommitment() {
            CreateCommitmentDto createCommitmentDto = new CreateCommitmentDto();
            doNothing().when(commitmentService).add(any(CreateCommitmentDto.class));

          commitmentController.add(createCommitmentDto);

            verify(commitmentService,times(1)).add(any(CreateCommitmentDto.class));
        }

    @Test
    void getAllCommitment() {
        id=UUID.randomUUID();
        CommitmentListiningDto commitmentListiningDto=new CommitmentListiningDto();
        commitmentListiningDto.setDurationMonths(12);
        commitmentListiningDto.setStartDate(LocalDate.now());
        commitmentListiningDto.setEndDate(LocalDate.now());
        commitmentListiningDto.setEarlyTerminationFee(100.0);
        commitmentListiningDto.setStatus(Status.ACTIVE);
        commitmentListiningDto.setContractDetailId(id);

        List<CommitmentListiningDto> commitmentListiningDtos= List.of(commitmentListiningDto);

        when(commitmentService.getAll()).thenReturn(commitmentListiningDtos);
        //Act
        List<CommitmentListiningDto> response= commitmentController.getAll();

        assertFalse(response.isEmpty());
        assertEquals(12,response.get(0).getDurationMonths());
        assertEquals(LocalDate.now(),response.get(0).getStartDate());
        assertEquals(LocalDate.now(),response.get(0).getEndDate());
        assertEquals(100.0,response.get(0).getEarlyTerminationFee());
        assertEquals(Status.ACTIVE,response.get(0).getStatus());
        assertNotNull(response.get(0).getContractDetailId());

        verify(commitmentService,times(1)).getAll();
    }

    @Test
    void updateContract() {
       id = UUID.randomUUID();
       contract_id=UUID.randomUUID();
        UpdateCommitmentDto updateCommitmentDto = new UpdateCommitmentDto();
        updateCommitmentDto.setContractDetailId(contract_id);
        updateCommitmentDto.setStatus(Status.ACTIVE);
        updateCommitmentDto.setStartDate(LocalDate.now());
        updateCommitmentDto.setEndDate(LocalDate.now());
        updateCommitmentDto.setId(id);
        updateCommitmentDto.setDurationMonths(12);
        updateCommitmentDto.setName("Test Name");
        updateCommitmentDto.setEarlyTerminationFee(100.0);
        updateCommitmentDto.setBillingDay(1);
        updateCommitmentDto.setCycleType(MONTHLY);

        when(commitmentService.update(any(UpdateCommitmentDto.class))).thenReturn(new Commitment());
        commitmentController.update(updateCommitmentDto);

        verify(commitmentService,times(1)).update(any(UpdateCommitmentDto.class));

    }

    @Test
    void deleteContract() {
        id = UUID.randomUUID();
        DeleteCommitmentDto deleteCommitmentDto= new DeleteCommitmentDto();
        deleteCommitmentDto.setId(id);
        doNothing().when(commitmentService).delete(any(DeleteCommitmentDto.class));
        commitmentController.delete(deleteCommitmentDto);
        verify(commitmentService,times(1)).delete(any(DeleteCommitmentDto.class));
    }


}



