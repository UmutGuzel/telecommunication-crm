package com.gygy.customersupportservice.service;

import com.gygy.customersupportservice.domain.TicketResponse;
import com.gygy.customersupportservice.repository.TicketResponseRepository;
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
public class TicketResponseService {
    private final TicketResponseRepository ticketResponseRepository;
    private final RuleEngine ruleEngine;

    @Transactional
    public TicketResponse createResponse(TicketResponse response) {
        // Process the response through the rule engine
        TicketResponse processedResponse = ruleEngine.processNewResponse(response);

        // Save to repository
        return ticketResponseRepository.save(processedResponse);
    }

    @Transactional(readOnly = true)
    public TicketResponse getResponseById(UUID id) {
        // Use optimized query that includes ticket information
        TicketResponse response = ticketResponseRepository.findByIdWithTicket(id);
        if (response == null) {
            throw new EntityNotFoundException("Response not found with id: " + id);
        }
        return response;
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> getResponsesByTicketId(UUID ticketId) {
        return ticketResponseRepository.findByTicketId(ticketId);
    }

    @Transactional(readOnly = true)
    public Page<TicketResponse> getResponsesByTicketId(UUID ticketId, Pageable pageable) {
        return ticketResponseRepository.findByTicketId(ticketId, pageable);
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> getLatestResponsesByTicketId(UUID ticketId, int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        return ticketResponseRepository.findLatestResponsesByTicketId(ticketId, pageable);
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> getResponsesByUserId(Long userId) {
        return ticketResponseRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Page<TicketResponse> getResponsesByUserId(Long userId, Pageable pageable) {
        return ticketResponseRepository.findByUserId(userId, pageable);
    }
}