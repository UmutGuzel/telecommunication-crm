package com.gygy.planservice.service;

import com.gygy.planservice.dto.ContractDto;
import com.gygy.planservice.dto.ContractRequestDto;

import java.util.List;

public interface ContractService {
    ContractDto createContract(ContractRequestDto requestDto);

    List<ContractDto> getAllContracts();
}