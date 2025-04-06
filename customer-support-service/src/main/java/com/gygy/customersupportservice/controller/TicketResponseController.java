package com.gygy.customersupportservice.controller;

import com.gygy.customersupportservice.domain.TicketResponse;
import com.gygy.customersupportservice.dto.TicketResponseDTO;
import com.gygy.customersupportservice.mapper.TicketResponseMapper;
import com.gygy.customersupportservice.service.TicketResponseService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ticket-responses")
@RequiredArgsConstructor
public class TicketResponseController {
    private final TicketResponseService ticketResponseService;
    private final TicketResponseMapper responseMapper;

    @PostMapping
    public ResponseEntity<TicketResponseDTO> createResponse(@RequestBody TicketResponseDTO responseDTO) {
        TicketResponse response = responseMapper.toEntity(responseDTO);
        TicketResponse savedResponse = ticketResponseService.createResponse(response);
        return ResponseEntity.ok(responseMapper.toDTO(savedResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getResponseById(@PathVariable UUID id) {
        TicketResponse response = ticketResponseService.getResponseById(id);
        return ResponseEntity.ok(responseMapper.toDTO(response));
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<TicketResponseDTO>> getResponsesByTicketId(@PathVariable UUID ticketId) {
        List<TicketResponse> responses = ticketResponseService.getResponsesByTicketId(ticketId);
        return ResponseEntity.ok(responseMapper.toDTOList(responses));
    }

    @GetMapping("/ticket/{ticketId}/paged")
    public ResponseEntity<Page<TicketResponseDTO>> getPagedResponsesByTicketId(
            @PathVariable UUID ticketId, Pageable pageable) {
        Page<TicketResponse> responsesPage = ticketResponseService.getResponsesByTicketId(ticketId, pageable);
        List<TicketResponseDTO> responseDTOs = responseMapper.toDTOList(responsesPage.getContent());
        return ResponseEntity.ok(new PageImpl<>(responseDTOs, pageable, responsesPage.getTotalElements()));
    }

    @GetMapping("/ticket/{ticketId}/latest")
    public ResponseEntity<List<TicketResponseDTO>> getLatestResponsesByTicketId(
            @PathVariable UUID ticketId, @RequestParam(defaultValue = "5") int limit) {
        List<TicketResponse> responses = ticketResponseService.getLatestResponsesByTicketId(ticketId, limit);
        return ResponseEntity.ok(responseMapper.toDTOList(responses));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketResponseDTO>> getResponsesByUserId(@PathVariable Long userId) {
        List<TicketResponse> responses = ticketResponseService.getResponsesByUserId(userId);
        return ResponseEntity.ok(responseMapper.toDTOList(responses));
    }

    @GetMapping("/user/{userId}/paged")
    public ResponseEntity<Page<TicketResponseDTO>> getPagedResponsesByUserId(
            @PathVariable Long userId, Pageable pageable) {
        Page<TicketResponse> responsesPage = ticketResponseService.getResponsesByUserId(userId, pageable);
        List<TicketResponseDTO> responseDTOs = responseMapper.toDTOList(responsesPage.getContent());
        return ResponseEntity.ok(new PageImpl<>(responseDTOs, pageable, responsesPage.getTotalElements()));
    }
}