package com.gygy.customersupportservice.controller;

import com.gygy.customersupportservice.domain.Ticket;
import com.gygy.customersupportservice.dto.TicketDTO;
import com.gygy.customersupportservice.dto.TicketReturnDTO;
import com.gygy.customersupportservice.mapper.TicketMapper;
import com.gygy.customersupportservice.service.TicketService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @PostMapping
    public ResponseEntity<TicketReturnDTO> createTicket(@RequestBody TicketDTO ticketDTO) {
        Ticket ticket = ticketMapper.toEntity(ticketDTO);
        Ticket savedTicket = ticketService.createTicket(ticket);
        return ResponseEntity.ok(ticketMapper.toDTO(savedTicket));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketReturnDTO> getTicket(@PathVariable UUID id) {
        Ticket ticket = ticketService.getTicket(id);
        return ResponseEntity.ok(ticketMapper.toDTO(ticket));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TicketReturnDTO>> getTicketsByCustomerId(@PathVariable UUID customerId) {
        List<Ticket> tickets = ticketService.getTicketsByCustomerId(customerId);
        return ResponseEntity.ok(ticketMapper.toDTOList(tickets));
    }

    @GetMapping("/customer/{customerId}/paged")
    public ResponseEntity<Page<TicketReturnDTO>> getPagedTicketsByCustomerId(
            @PathVariable UUID customerId, Pageable pageable) {
        Page<Ticket> ticketsPage = ticketService.getTicketsByCustomerId(customerId, pageable);
        List<TicketReturnDTO> ticketDTOs = ticketMapper.toDTOList(ticketsPage.getContent());
        return ResponseEntity.ok(new PageImpl<>(ticketDTOs, pageable, ticketsPage.getTotalElements()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketReturnDTO>> getTicketsByUserId(@PathVariable UUID userId) {
        List<Ticket> tickets = ticketService.getTicketsByUserId(userId);
        return ResponseEntity.ok(ticketMapper.toDTOList(tickets));
    }

    @GetMapping("/user/{userId}/paged")
    public ResponseEntity<Page<TicketReturnDTO>> getPagedTicketsByUserId(
            @PathVariable UUID userId, Pageable pageable) {
        Page<Ticket> ticketsPage = ticketService.getTicketsByUserId(userId, pageable);
        List<TicketReturnDTO> ticketDTOs = ticketMapper.toDTOList(ticketsPage.getContent());
        return ResponseEntity.ok(new PageImpl<>(ticketDTOs, pageable, ticketsPage.getTotalElements()));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TicketReturnDTO>> getTicketsByStatus(@PathVariable String status) {
        List<Ticket> tickets = ticketService.getTicketsByStatus(status);
        return ResponseEntity.ok(ticketMapper.toDTOList(tickets));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<TicketReturnDTO>> getTicketsByType(
            @PathVariable String type, Pageable pageable) {
        Page<Ticket> ticketsPage = ticketService.getTicketsByType(type, pageable);
        List<TicketReturnDTO> ticketDTOs = ticketMapper.toDTOList(ticketsPage.getContent());
        return ResponseEntity.ok(new PageImpl<>(ticketDTOs, pageable, ticketsPage.getTotalElements()));
    }

    @PutMapping("/{ticketId}/status/{status}")
    public ResponseEntity<TicketReturnDTO> updateTicketStatus(
            @PathVariable UUID ticketId,
            @PathVariable String status) {
        Ticket updatedTicket = ticketService.updateTicketStatus(ticketId, status);
        return ResponseEntity.ok(ticketMapper.toDTO(updatedTicket));
    }
}