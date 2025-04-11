package com.gygy.common.events.userservice;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleChangedEvent {
    private String email;
    private List<String> roles;
}
