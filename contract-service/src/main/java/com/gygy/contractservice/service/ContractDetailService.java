package com.gygy.contractservice.service;

import com.gygy.contractservice.dto.contractDetail.ContractDetailListiningDto;
import com.gygy.contractservice.dto.contractDetail.CreateContractDetailDto;
import com.gygy.contractservice.dto.contractDetail.DeleteContractDetailDto;
import com.gygy.contractservice.dto.contractDetail.UpdateContractDetailDto;
import com.gygy.contractservice.entity.ContractDetail;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ContractDetailService {
    ContractDetail findById(UUID id);
    void add(CreateContractDetailDto createContractDetailDto);
    List<ContractDetailListiningDto> getAll();
    ContractDetail update(UpdateContractDetailDto updateContractDetailDto);
    void delete(DeleteContractDetailDto deleteAirlineDto);
    List<ContractDetailListiningDto> getContractByDate(UUID id, LocalDate startDate,LocalDate endDate);
    List<ContractDetailListiningDto> getIndividualContractDetails();
    List<ContractDetailListiningDto> getCorporateContractDetails();
    List<ContractDetailListiningDto> getContractsByCustomerId(UUID customerId);
    List<ContractDetailListiningDto> getExpiredContractsByCustomerId(UUID customerId);
    List<ContractDetailListiningDto> getContractsByMonthAndYear(int month, int year);



}
