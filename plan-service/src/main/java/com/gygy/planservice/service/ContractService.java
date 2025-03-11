package com.gygy.planservice.service;

import com.gygy.planservice.dto.ContractDto;
import com.gygy.planservice.dto.ContractRequestDto;

import java.util.List;
import java.util.UUID;

public interface ContractService {
    ContractDto createContract(ContractRequestDto requestDto);

    List<ContractDto> getAllContracts();

    ContractDto getContractById(UUID id);

    ContractDto updateContract(UUID id, ContractRequestDto requestDto);

    void deleteContract(UUID id);
}