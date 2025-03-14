package com.gygy.contractservice.service.impl;

import com.gygy.contractservice.dto.contractDetail.ContractDetailListiningDto;
import com.gygy.contractservice.dto.contractDetail.CreateContractDetailDto;
import com.gygy.contractservice.dto.contractDetail.DeleteContractDetailDto;
import com.gygy.contractservice.dto.contractDetail.UpdateContractDetailDto;
import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.repository.ContractDetailRepository;
import com.gygy.contractservice.service.CommitmentService;
import com.gygy.contractservice.service.ContractDetailService;
import com.gygy.contractservice.service.ContractService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContractDetailServiceImpl implements ContractDetailService {

    private final ContractDetailRepository contractDetailRepository;
    private final ContractService contractService;
    private final CommitmentService commitmentService;

    public ContractDetailServiceImpl(ContractDetailRepository contractDetailRepository, ContractService contractService, CommitmentService commitmentService) {
        this.contractDetailRepository = contractDetailRepository;
        this.contractService = contractService;
        this.commitmentService = commitmentService;
    }

    @Override
    public ContractDetail findById(UUID id) {return contractDetailRepository.findById(id).orElseThrow(()-> new RuntimeException("Contract Detail not found"));}


    @Override
    public void add(CreateContractDetailDto createContractDetailDto) {
        Contract contract=contractService.findById(createContractDetailDto.getContractId()).orElseThrow(()-> new RuntimeException("Contract not found"));
        ContractDetail contractDetail = new ContractDetail();
        contractDetail.setContract(contract);
        contractDetail.setCustomerId(createContractDetailDto.getCustomerId());
        contractDetail.setContractDetailType(createContractDetailDto.getContractDetailType());
        contractDetail.setServiceType(createContractDetailDto.getServiceType());
        contractDetail.setCreatedAt(LocalDateTime.now());
        contractDetail.setStatus(createContractDetailDto.getStatus());
        contractDetail.setStartDate(createContractDetailDto.getStartDate());
        contractDetail.setEndDate(createContractDetailDto.getEndDate());
        contractDetailRepository.save(contractDetail);

    }

    @Override
    public List<ContractDetailListiningDto> getAll() {
        List<ContractDetailListiningDto> contractDetailListiningDtos=
                contractDetailRepository
                        .findAll()
                        .stream()
                        .map((contractDetail) ->new ContractDetailListiningDto(contractDetail.getContractDetailType()
                                ,contractDetail.getContract().getId(),contractDetail.getStatus(),contractDetail.getEndDate(),contractDetail.getStartDate())).toList();

        return contractDetailListiningDtos;
    }

    @Override
    public ContractDetail update(UpdateContractDetailDto updateContractDetailDto) {
        Contract contract=contractService.findById(updateContractDetailDto.getContractId()).orElseThrow(()-> new RuntimeException("Contract not found"));
        ContractDetail contractDetail=contractDetailRepository.findById(updateContractDetailDto.getId()).orElseThrow(()->new RuntimeException("Contract detail not found"));
        contractDetail.setServiceType(updateContractDetailDto.getServiceType());
        contractDetail.setContract(contract);
        contractDetail.setStartDate(updateContractDetailDto.getStartDate());
        contractDetail.setEndDate(updateContractDetailDto.getEndDate());
        contractDetail.setUpdatedAt(LocalDateTime.now());
        return contractDetailRepository.save(contractDetail);
    }

    @Override
    public void delete(DeleteContractDetailDto deleteContractDetailDto) {
        ContractDetail contractDetail=contractDetailRepository.findById(deleteContractDetailDto.getId()).orElseThrow(()->new RuntimeException("Contract detail not found"));
        contractDetailRepository.delete(contractDetail);

    }
}
