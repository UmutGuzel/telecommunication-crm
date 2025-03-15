package com.gygy.contractservice.controller;

import com.gygy.contractservice.dto.contract.ContractListiningDto;
import com.gygy.contractservice.dto.contract.CreateContractDto;
import com.gygy.contractservice.dto.contract.DeleteContractDto;
import com.gygy.contractservice.dto.contract.UpdateContractDto;
import com.gygy.contractservice.service.ContractService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contracts")
public class ContractController {
    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }
    @GetMapping
    public List<ContractListiningDto> getAll() {
        return contractService.getAll();
    }
    @PostMapping
    public void add(@RequestBody CreateContractDto createContractDto) {
        contractService.add(createContractDto);
    }
    @PutMapping
    public void update(@RequestBody UpdateContractDto updateContractDto) {
        contractService.update(updateContractDto);
    }
    @DeleteMapping
    public void delete(@RequestBody DeleteContractDto deleteContractDto) {
        contractService.delete(deleteContractDto);
    }
}
