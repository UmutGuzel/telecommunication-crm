package com.gygy.userservice.application.user.query.GetUserList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserListDto {
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private boolean active;
    private List<String> roles;
    private List<String> permissions;
}