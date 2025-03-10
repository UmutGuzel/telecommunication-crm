package com.gygy.planservice.controller;

import com.gygy.planservice.dto.ContractDto;
import com.gygy.planservice.dto.ContractRequestDto;
import com.gygy.planservice.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/plan-contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public ResponseEntity<ContractDto> createContract(@RequestBody ContractRequestDto requestDto) {
        ContractDto createdContract = contractService.createContract(requestDto);
        return new ResponseEntity<>(createdContract, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ContractDto>> getAllContracts() {
        return ResponseEntity.ok(contractService.getAllContracts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractDto> getContractById(@PathVariable UUID id) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContract(@PathVariable UUID id, @RequestBody ContractRequestDto requestDto) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContract(@PathVariable UUID id) {
        return ResponseEntity.noContent().build();
    }
}