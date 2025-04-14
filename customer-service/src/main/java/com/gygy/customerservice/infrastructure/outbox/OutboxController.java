package com.gygy.customerservice.infrastructure.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/outbox")
@RequiredArgsConstructor
public class OutboxController {

    private final OutboxService outboxService;

    @PutMapping("/{id}/mark-as-processed")
    public ResponseEntity<Void> markAsProcessed(@PathVariable("id") UUID id) {
        log.info("Marking outbox event as processed: {}", id);
        outboxService.markAsProcessed(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/mark-as-failed")
    public ResponseEntity<Void> markAsFailed(@PathVariable("id") UUID id) {
        log.info("Marking outbox event as failed: {}", id);
        outboxService.markAsFailed(id);
        return ResponseEntity.ok().build();
    }
} 