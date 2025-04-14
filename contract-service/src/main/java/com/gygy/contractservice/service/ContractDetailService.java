package com.gygy.contractservice.service;

import com.gygy.contractservice.dto.contractDetail.ContractDetailListiningDto;
import com.gygy.contractservice.dto.contractDetail.DeleteContractDetailDto;
import com.gygy.contractservice.dto.contractDetail.UpdateContractDetailDto;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.entity.BillingPlan;
import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.dto.contract.CreateContractDto;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ContractDetailService {
    ContractDetail findById(UUID id);

    void add(BillingPlan billingPlan, Contract contract, CreateContractDto createContractDto);

    List<ContractDetailListiningDto> getAll();

    ContractDetail update(UpdateContractDetailDto updateContractDetailDto);

    void delete(DeleteContractDetailDto deleteAirlineDto);

    List<ContractDetailListiningDto> getContractByDate(UUID id, LocalDate startDate, LocalDate endDate);

    List<ContractDetailListiningDto> getIndividualContractDetails();

    List<ContractDetailListiningDto> getCorporateContractDetails();

    List<ContractDetailListiningDto> getContractsByCustomerId(UUID customerId);

    List<ContractDetailListiningDto> getExpiredContractsByCustomerId(UUID customerId);

    List<ContractDetailListiningDto> getContractsByMonthAndYear(int month, int year);

}
