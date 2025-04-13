package com.gygy.customersupportservice.service;

import com.gygy.customersupportservice.domain.Ticket;
import com.gygy.customersupportservice.domain.TicketStatus;
import com.gygy.customersupportservice.repository.TicketRepository;
import com.gygy.customersupportservice.rule.TicketRules;
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
    private final AnalyticsEventService analyticsEventService;
    private final TicketRules ticketRules;

    public Ticket createTicket(Ticket ticket) {
        Ticket processedTicket = ticketRules.applyNewTicketRules(ticket);
        // Save the ticket directly
        Ticket savedTicket = ticketRepository.save(processedTicket);

        // Publish analytics event
        analyticsEventService.publishTicketCreatedEvent(savedTicket);

        return savedTicket;
    }

    public Ticket getTicket(UUID id) {
        // Use the optimized query that fetches the status eagerly
        Ticket ticket = ticketRepository.findByIdWithStatus(id);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket not found with id: " + id);
        }
        return ticket;
    }

    public List<Ticket> getTicketsByCustomerId(Long customerId) {
        return ticketRepository.findByCustomerId(customerId);
    }

    public Page<Ticket> getTicketsByCustomerId(Long customerId, Pageable pageable) {
        return ticketRepository.findByCustomerId(customerId, pageable);
    }

    public List<Ticket> getTicketsByUserId(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    public Page<Ticket> getTicketsByUserId(Long userId, Pageable pageable) {
        return ticketRepository.findByUserId(userId, pageable);
    }

    public List<Ticket> getTicketsByStatus(String status) {
        return ticketRepository.findByStatusName(status);
    }

    public Page<Ticket> getTicketsByType(String type, Pageable pageable) {
        return ticketRepository.findByType(type, pageable);
    }

    public Ticket updateTicketStatus(UUID ticketId, String newStatusName) {
        Ticket ticket = getTicket(ticketId);
        String previousStatus = ticket.getStatus().getStatus();
        ticketRules.validateTicket(ticket);
        Ticket processedTicket = ticketRules.applyStatusChangeRules(ticket, newStatusName);
        Ticket savedTicket = ticketRepository.save(processedTicket);

        // Publish status change analytics event
        analyticsEventService.publishTicketStatusChangeEvent(savedTicket, previousStatus);

        return savedTicket;
    }
}