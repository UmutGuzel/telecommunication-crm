package com.gygy.customersupportservice.service;

import com.gygy.customersupportservice.domain.Ticket;
import com.gygy.customersupportservice.repository.TicketRepository;
import com.gygy.customersupportservice.rule.RuleEngine;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final RuleEngine ruleEngine;

    @Transactional
    public Ticket createTicket(Ticket ticket) {
        // Process the ticket through the rule engine
        Ticket processedTicket = ruleEngine.processNewTicket(ticket);
        return ticketRepository.save(processedTicket);
    }

    @Transactional(readOnly = true)
    public Ticket getTicket(UUID id) {
        // Use the optimized query that fetches the status eagerly
        Ticket ticket = ticketRepository.findByIdWithStatus(id);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket not found with id: " + id);
        }
        return ticket;
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByCustomerId(Long customerId) {
        return ticketRepository.findByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public Page<Ticket> getTicketsByCustomerId(Long customerId, Pageable pageable) {
        return ticketRepository.findByCustomerId(customerId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByUserId(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Page<Ticket> getTicketsByUserId(Long userId, Pageable pageable) {
        return ticketRepository.findByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByStatus(String status) {
        return ticketRepository.findByStatusName(status);
    }

    @Transactional(readOnly = true)
    public Page<Ticket> getTicketsByType(String type, Pageable pageable) {
        return ticketRepository.findByType(type, pageable);
    }

    @Transactional
    public Ticket updateTicketStatus(UUID ticketId, String status) {
        Ticket ticket = getTicket(ticketId);
        // Process the status change through the rule engine
        Ticket updatedTicket = ruleEngine.processTicketStatusChange(ticket, status);
        return ticketRepository.save(updatedTicket);
    }
}