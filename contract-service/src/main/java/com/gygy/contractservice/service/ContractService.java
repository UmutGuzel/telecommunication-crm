package com.gygy.contractservice.service;

import com.gygy.contractservice.dto.contract.ContractListiningDto;
import com.gygy.contractservice.dto.contract.CreateContractDto;
import com.gygy.contractservice.dto.contract.DeleteContractDto;
import com.gygy.contractservice.dto.contract.UpdateContractDto;
import com.gygy.contractservice.entity.Contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContractService {
    Optional<Contract> findById(UUID id);
    void add(CreateContractDto createContractDto);
    List<ContractListiningDto> getAll();
    Contract update(UpdateContractDto updateContractDto);
    void delete(DeleteContractDto deleteContractDto);
}
