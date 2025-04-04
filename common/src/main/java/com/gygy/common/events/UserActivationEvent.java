package com.gygy.common.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivationEvent {
    private String email;
    private String name;
    private LocalDateTime activationDate;
    private String activationLink;
}