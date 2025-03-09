package com.gygy.planservice.service;

import com.gygy.planservice.dto.ContractDto;
import com.gygy.planservice.dto.ContractRequestDto;

public interface ContractService {
    ContractDto createContract(ContractRequestDto requestDto);
}