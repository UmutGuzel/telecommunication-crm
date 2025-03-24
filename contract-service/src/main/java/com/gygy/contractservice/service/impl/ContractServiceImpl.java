package com.gygy.contractservice.service.impl;

import com.gygy.contractservice.dto.contract.ContractListiningDto;
import com.gygy.contractservice.dto.contract.CreateContractDto;
import com.gygy.contractservice.dto.contract.DeleteContractDto;
import com.gygy.contractservice.dto.contract.UpdateContractDto;
import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.repository.ContractRepository;
import com.gygy.contractservice.service.ContractService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;

    public ContractServiceImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Override
    public Optional<Contract> findById(UUID id) {
        return contractRepository.findById(id);
    }

    @Override
    public void add(CreateContractDto createContractDto) {
        Contract contract = new Contract();
        contract.setContractNumber(createContractDto.getContractNumber());
        contract.setStatus(createContractDto.getStatus());
        contract.setCreatedAt(LocalDateTime.now());
        contract.setUpdatedAt(LocalDateTime.now());
        contract.setDocumentType(createContractDto.getDocumentType());
        contract.setSignatureDate(createContractDto.getSignatureDate());
        contract.setUploadDate(createContractDto.getUploadDate());
        contract.setDocumentUrl(createContractDto.getDocumentUrl());
        contractRepository.save(contract);

    }

    @Override
    public List<ContractListiningDto> getAll() {
        List<ContractListiningDto> contractListiningDtos =
                contractRepository
                        .findAll()
                        .stream()
                        .map(contract -> new ContractListiningDto(contract.getContractNumber()
                                , contract.getStatus()
                                , contract.getDocumentType()
                                , contract.getDocumentUrl()
                                , contract.getSignatureDate())).toList();
        return contractListiningDtos;
    }

    @Override
    public Contract update(UpdateContractDto updateContractDto) {
        Contract contract = contractRepository.findById(updateContractDto.getId()).orElseThrow(() -> new RuntimeException("Contract not found"));
        contract.setContractNumber(updateContractDto.getContractNumber());
        contract.setStatus(updateContractDto.getStatus());
        contract.setDocumentUrl(updateContractDto.getDocumentUrl());
        contract.setSignatureDate(updateContractDto.getSignatureDate());
        contract.setUploadDate(updateContractDto.getUploadDate());
        contract.setDocumentType(updateContractDto.getDocumentType());

        return contractRepository.save(contract);
    }

    @Override
    public void delete(DeleteContractDto deleteContractDto) {
        Contract contract = contractRepository.findById(deleteContractDto.getId()).orElseThrow(() -> new RuntimeException("Contract not found"));
        contractRepository.delete(contract);

    }

    @Override
    public List<ContractListiningDto> getActiveContracts() {
        return contractRepository.findAll().stream()
                .filter(contract -> "ACTIVE".equals(contract.getStatus().toString()))
                .map(contract -> new ContractListiningDto(
                        contract.getContractNumber(),
                        contract.getStatus(),
                        contract.getDocumentType(),
                        contract.getDocumentUrl(),
                        contract.getSignatureDate()))
                .toList();
    }

    @Override
    public List<ContractListiningDto> getSuspendedContracts() {
        return contractRepository.findAll().stream()
                .filter(contract -> "SUSPENDED".equals(contract.getStatus().toString()))
                .map(contract -> new ContractListiningDto(
                        contract.getContractNumber(),
                        contract.getStatus(),
                        contract.getDocumentType(),
                        contract.getDocumentUrl(),
                        contract.getSignatureDate()))
                .toList();

    }
}
