package com.gygy.planservice.mapper;

import com.gygy.planservice.dto.ContractDto;
import com.gygy.planservice.dto.ContractRequestDto;
import com.gygy.planservice.entity.Contract;
import com.gygy.planservice.entity.Plan;
import org.springframework.stereotype.Component;

@Component
public class ContractMapper {

    private final PlanMapper planMapper;

    public ContractMapper(PlanMapper planMapper) {
        this.planMapper = planMapper;
    }

    public Contract toEntity(ContractRequestDto dto, Plan plan) {
        Contract contract = new Contract();
        contract.setType(dto.getType());
        contract.setDiscount(dto.getDiscount());
        contract.setPlan(plan);
        return contract;
    }

    public ContractDto toDto(Contract entity) {
        return new ContractDto(
                entity.getId(),
                entity.getType(),
                entity.getDiscount(),
                planMapper.toDto(entity.getPlan()));
    }
}