package com.gygy.contractservice.service;

import com.gygy.contractservice.dto.commitment.CommitmentListiningDto;
import com.gygy.contractservice.dto.commitment.CreateCommitmentDto;
import com.gygy.contractservice.dto.commitment.DeleteCommitmentDto;
import com.gygy.contractservice.dto.commitment.UpdateCommitmentDto;
import com.gygy.contractservice.entity.Commitment;
import java.util.List;
import java.util.UUID;

public interface CommitmentService {
    Commitment findById(UUID id);
    void add(CreateCommitmentDto createCommitmentDto);
    List<CommitmentListiningDto> getAll();
    Commitment update(UpdateCommitmentDto updateCommitmentDto);
    void delete(DeleteCommitmentDto deleteCommitmentDto);
}
