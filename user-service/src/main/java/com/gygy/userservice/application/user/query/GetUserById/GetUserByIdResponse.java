package com.gygy.userservice.application.user.query.GetUserById;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserByIdResponse {
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate birthDate;
    private boolean active;
    private List<String> roles;
    private List<String> permissions;
}