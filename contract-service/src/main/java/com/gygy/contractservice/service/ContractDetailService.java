package com.gygy.contractservice.service;

import com.gygy.contractservice.dto.contractDetail.ContractDetailListiningDto;
import com.gygy.contractservice.dto.contractDetail.CreateContractDetailDto;
import com.gygy.contractservice.dto.contractDetail.DeleteContractDetailDto;
import com.gygy.contractservice.dto.contractDetail.UpdateContractDetailDto;
import com.gygy.contractservice.entity.ContractDetail;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContractDetailService {
    ContractDetail findById(UUID id);
    void add(CreateContractDetailDto createContractDetailDto);
    List<ContractDetailListiningDto> getAll();
    ContractDetail update(UpdateContractDetailDto updateContractDetailDto);
    void delete(DeleteContractDetailDto deleteAirlineDto);
}
