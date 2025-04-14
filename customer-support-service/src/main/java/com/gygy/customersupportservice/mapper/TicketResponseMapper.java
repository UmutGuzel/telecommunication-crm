package com.gygy.customersupportservice.mapper;

import com.gygy.customersupportservice.domain.Ticket;
import com.gygy.customersupportservice.domain.TicketResponse;
import com.gygy.customersupportservice.dto.TicketResponseDTO;
import com.gygy.customersupportservice.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TicketResponseMapper {
    private final TicketRepository ticketRepository;

    public TicketResponseDTO toDTO(TicketResponse response) {
        if (response == null) {
            return null;
        }

        return TicketResponseDTO.builder()
                .id(response.getId())
                .message(response.getMessage())
                .userId(response.getUserId())
                .ticketId(response.getTicket() != null ? response.getTicket().getId() : null)
                .createdAt(response.getCreatedAt())
                .build();
    }

    public TicketResponse toEntity(TicketResponseDTO dto) {
        if (dto == null) {
            return null;
        }

        TicketResponse response = new TicketResponse();
        response.setId(dto.getId());
        response.setMessage(dto.getMessage());
        response.setUserId(dto.getUserId());

        if (dto.getTicketId() != null) {
            Ticket ticket = ticketRepository.findById(dto.getTicketId())
                    .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + dto.getTicketId()));
            response.setTicket(ticket);
        }

        response.setCreatedAt(dto.getCreatedAt());

        return response;
    }

    public List<TicketResponseDTO> toDTOList(List<TicketResponse> responses) {
        if (responses == null) {
            return null;
        }

        return responses.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}