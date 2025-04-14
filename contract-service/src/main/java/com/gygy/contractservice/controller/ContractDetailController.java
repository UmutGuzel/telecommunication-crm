package com.gygy.contractservice.controller;

import com.gygy.contractservice.dto.contractDetail.ContractDetailListiningDto;
import com.gygy.contractservice.dto.contractDetail.CreateContractDetailDto;
import com.gygy.contractservice.dto.contractDetail.DeleteContractDetailDto;
import com.gygy.contractservice.dto.contractDetail.UpdateContractDetailDto;
import com.gygy.contractservice.service.ContractDetailService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contract-details")
public class ContractDetailController {
    private final ContractDetailService contractDetailService;

    public ContractDetailController(ContractDetailService contractDetailService) {
        this.contractDetailService = contractDetailService;
    }

    @GetMapping
    public List<ContractDetailListiningDto> getAll() {
        return contractDetailService.getAll();
    }

    // @PostMapping
    // public void add(@RequestBody CreateContractDetailDto createContractDetailDto)
    // {
    // contractDetailService.add(createContractDetailDto);
    // }
    @PutMapping
    public void update(@RequestBody UpdateContractDetailDto updateContractDetailDto) {
        contractDetailService.update(updateContractDetailDto);
    }

    @DeleteMapping
    public void delete(@RequestBody DeleteContractDetailDto deleteContractDetailDto) {
        contractDetailService.delete(deleteContractDetailDto);
    }

    @GetMapping("/getContractByDate")
    public List<ContractDetailListiningDto> getContractByDate(@RequestParam UUID id, @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return contractDetailService.getContractByDate(id, startDate, endDate);
    }

    @GetMapping("/getIndividualContractDetails")
    public List<ContractDetailListiningDto> getIndividualContractDetails() {
        return contractDetailService.getIndividualContractDetails();
    }

    @GetMapping("/getCoporateContractDetails")
    public List<ContractDetailListiningDto> getCoporateContractDetails() {
        return contractDetailService.getCorporateContractDetails();
    }

    @GetMapping("/getContractsByCustomerId")
    public List<ContractDetailListiningDto> getContractsByCustomerId(@RequestParam UUID id) {
        return contractDetailService.getContractsByCustomerId(id);
    }

    @GetMapping("/getExpiredContractsByCustomerId")
    public List<ContractDetailListiningDto> getExpiredContractsByCustomerId(@RequestParam UUID id) {
        return contractDetailService.getExpiredContractsByCustomerId(id);
    }

    @GetMapping("/getContractsByMonthAndYear")
    public List<ContractDetailListiningDto> getContractsByMonthAndYear(@RequestParam int month, int year) {
        return contractDetailService.getContractsByMonthAndYear(month, year);
    }

}
