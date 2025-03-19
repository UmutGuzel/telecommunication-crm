package com.gygy.planservice.controller;

import com.gygy.planservice.dto.ContractDto;
import com.gygy.planservice.dto.ContractRequestDto;
import com.gygy.planservice.service.ContractService;

import jakarta.validation.Valid;
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
    public ResponseEntity<ContractDto> createContract(@Valid @RequestBody ContractRequestDto requestDto) {
        return new ResponseEntity<>(contractService.createContract(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ContractDto>> getAllContracts() {
        return ResponseEntity.ok(contractService.getAllContracts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractDto> getContractById(@PathVariable UUID id) {
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContractDto> updateContract(
            @PathVariable UUID id,
            @Valid @RequestBody ContractRequestDto requestDto) {
        return ResponseEntity.ok(contractService.updateContract(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable UUID id) {
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<ContractDto>> getAllActiveContracts() {
        return ResponseEntity.ok(contractService.getAllActiveContracts());
    }
}