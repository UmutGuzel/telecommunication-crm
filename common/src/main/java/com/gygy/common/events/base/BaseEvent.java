package com.gygy.common.events.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEvent {
    private UUID eventId;
    private LocalDateTime eventTime;
    private String serviceName;
    private UUID customerId;
    //private String correlationId;  işlemleri takip etmek için kullanılabiir, hata tespiti kolaylığı
}
