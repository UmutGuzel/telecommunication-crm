package com.gygy.planservice.service;

import com.gygy.planservice.dto.ContractDto;
import com.gygy.planservice.dto.ContractRequestDto;
import com.gygy.planservice.entity.Contract;
import com.gygy.planservice.entity.Plan;
import com.gygy.planservice.exception.ContractNotFoundException;
import com.gygy.planservice.exception.ContractRequestNullException;
import com.gygy.planservice.exception.ContractIdNullException;
import com.gygy.planservice.exception.PlanIdNullException;
import com.gygy.planservice.exception.PlanNotFoundException;
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
        if (requestDto == null) {
            throw new ContractRequestNullException();
        }
        if (requestDto.getPlanId() == null) {
            throw new PlanIdNullException();
        }

        Plan plan = planRepository.findById(requestDto.getPlanId())
                .orElseThrow(() -> new PlanNotFoundException("Plan not found with id: " + requestDto.getPlanId()));

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
        if (id == null) {
            throw new ContractIdNullException();
        }

        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Contract not found with id: " + id));
        return contractMapper.toDto(contract);
    }

    @Override
    @Transactional
    public ContractDto updateContract(UUID id, ContractRequestDto requestDto) {
        if (id == null) {
            throw new ContractIdNullException();
        }
        if (requestDto == null) {
            throw new ContractRequestNullException();
        }
        if (requestDto.getPlanId() == null) {
            throw new PlanIdNullException();
        }

        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Contract not found with id: " + id));

        Plan plan = planRepository.findById(requestDto.getPlanId())
                .orElseThrow(() -> new PlanNotFoundException("Plan not found with id: " + requestDto.getPlanId()));

        contract.setType(requestDto.getType());
        contract.setDiscount(requestDto.getDiscount());
        contract.setPlan(plan);

        Contract updatedContract = contractRepository.save(contract);
        return contractMapper.toDto(updatedContract);
    }

    @Override
    @Transactional
    public void deleteContract(UUID id) {
        if (id == null) {
            throw new ContractIdNullException();
        }

        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Contract not found with id: " + id));

        contract.setIsPassive(true);
        contractRepository.save(contract);
    }

    @Override
    public List<ContractDto> getAllActiveContracts() {
        return contractRepository.findAllByIsPassiveFalse().stream()
                .map(contractMapper::toDto)
                .toList();
    }
}