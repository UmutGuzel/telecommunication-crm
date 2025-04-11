package com.gygy.notificationservice.tempdto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleChangedEvent {
    private String email;
    private List<String> roles;
}
