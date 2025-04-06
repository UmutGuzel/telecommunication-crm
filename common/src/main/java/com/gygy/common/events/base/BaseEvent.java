package com.gygy.common.events.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class BaseEvent {
    private UUID eventId = UUID.randomUUID();
    private LocalDateTime eventTime = LocalDateTime.now();
    private String serviceName;
    private UUID customerId;
}
