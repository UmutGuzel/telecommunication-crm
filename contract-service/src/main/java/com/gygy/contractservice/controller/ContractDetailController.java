package com.gygy.contractservice.controller;

import com.gygy.contractservice.dto.contractDetail.ContractDetailListiningDto;
import com.gygy.contractservice.dto.contractDetail.CreateContractDetailDto;
import com.gygy.contractservice.dto.contractDetail.DeleteContractDetailDto;
import com.gygy.contractservice.dto.contractDetail.UpdateContractDetailDto;
import com.gygy.contractservice.service.ContractDetailService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contract-details")
public class ContractDetailController {
    private final ContractDetailService contractDetailService;

    public ContractDetailController(ContractDetailService contractDetailService) {
        this.contractDetailService = contractDetailService;
    }
    @GetMapping
    public List<ContractDetailListiningDto> getAll(){
        return contractDetailService.getAll();
    }
    @PostMapping
    public void add(@RequestBody CreateContractDetailDto createContractDetailDto) {
        contractDetailService.add(createContractDetailDto);
    }
    @PutMapping
    public void update(@RequestBody UpdateContractDetailDto updateContractDetailDto) {
        contractDetailService.update(updateContractDetailDto);
    }
    @DeleteMapping
    public void delete(@RequestBody DeleteContractDetailDto deleteContractDetailDto) {
        contractDetailService.delete(deleteContractDetailDto);
    }
}
