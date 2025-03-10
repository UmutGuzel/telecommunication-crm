package com.gygy.planservice.service;

import com.gygy.planservice.dto.ContractDto;
import com.gygy.planservice.dto.ContractRequestDto;
import com.gygy.planservice.entity.Contract;
import com.gygy.planservice.entity.Plan;
import com.gygy.planservice.mapper.ContractMapper;
import com.gygy.planservice.repository.ContractRepository;
import com.gygy.planservice.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final PlanRepository planRepository;
    private final ContractMapper contractMapper;

    @Override
    @Transactional
    public ContractDto createContract(ContractRequestDto requestDto) {
        Plan plan = planRepository.findById(requestDto.getPlanId())
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + requestDto.getPlanId()));

        Contract contract = contractMapper.toEntity(requestDto, plan);
        Contract savedContract = contractRepository.save(contract);
        return contractMapper.toDto(savedContract);
    }

    @Override
    public List<ContractDto> getAllContracts() {
        return contractRepository.findAll().stream()
                .map(contractMapper::toDto)
                .toList();
    }

    @Override
    public ContractDto getContractById(UUID id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));
        return contractMapper.toDto(contract);
    }
}