package com.gygy.customersupportservice.service;

import org.springframework.stereotype.Service;

import com.gygy.customersupportservice.domain.TicketStatus;
import com.gygy.customersupportservice.repository.TicketStatusRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketStatusService {
    private final TicketStatusRepository ticketStatusRepository;

    public TicketStatus findByStatus(String status) {
        return ticketStatusRepository.findByStatus(status)
                .orElseThrow(() -> new EntityNotFoundException("Ticket status not found: " + status));
    }
}