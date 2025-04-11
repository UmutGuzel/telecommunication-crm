package com.gygy.userservice.application.user.mapper;

import org.springframework.stereotype.Component;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.application.user.command.CreateUser.CreateUserCommand;
import com.gygy.userservice.application.user.command.UpdateUser.UpdateUserCommand;
import com.gygy.userservice.application.user.command.ChangePassword.ChangePasswordCommand;
import com.gygy.userservice.core.configuration.ApplicationConfig;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.application.user.query.GetUserList.GetUserListDto;
import com.gygy.userservice.application.user.query.GetUserById.GetUserByIdResponse;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@Data
@RequiredArgsConstructor
public class UserMapper {
    private final ApplicationConfig applicationConfig;

    public User toEntity(CreateUserCommand command) {
        // Generate activation token
        String activationToken = UUID.randomUUID().toString();

        return User.builder()
                .name(command.getName())
                .surname(command.getSurname())
                .email(command.getEmail())
                .password(command.getPassword())
                .phoneNumber(command.getPhoneNumber())
                .address(command.getAddress())
                .active(false)
                .activationToken(activationToken)
                .activationTokenExpiry(LocalDateTime.now().plusHours(applicationConfig.getActivationTokenExpiryHours()))
                .build();
    }

    public String generateActivationLink(User user) {
        return applicationConfig.getGatewayUrl() +
                applicationConfig.getActivationPath() +
                "?token=" + user.getActivationToken();
    }

    public User toEntity(ChangePasswordCommand command) {
        return User.builder()
                .email(command.getEmail())
                .password(command.getNewPassword())
                .build();
    }

    public void updateEntity(User user, UpdateUserCommand command) {
        if (command.getName() != null && !command.getName().isEmpty()) {
            user.setName(command.getName());
        }
        if (command.getSurname() != null && !command.getSurname().isEmpty()) {
            user.setSurname(command.getSurname());
        }
        if (command.getEmail() != null && !command.getEmail().isEmpty()) {
            user.setEmail(command.getEmail());
        }
        if (command.getPhoneNumber() != null && !command.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(command.getPhoneNumber());
        }
        if (command.getAddress() != null && !command.getAddress().isEmpty()) {
            user.setAddress(command.getAddress());
        }
    }

    public com.gygy.common.entity.User toCommonUser(com.gygy.userservice.entity.User user) {
        if (user == null) {
            return null;
        }

        com.gygy.common.entity.User commonUser = new com.gygy.common.entity.User();
        commonUser.setId(user.getId());
        commonUser.setEmail(user.getEmail());

        Set<com.gygy.common.entity.Role> commonRoles = new HashSet<>();
        if (user.getRoles() != null) {
            commonRoles = user.getRoles().stream()
                    .map(this::toCommonRole)
                    .collect(Collectors.toSet());
        }
        commonUser.setRoles(commonRoles);

        Set<com.gygy.common.entity.Permission> commonPermissions = new HashSet<>();
        if (user.getPermissions() != null) {
            commonPermissions = user.getPermissions().stream()
                    .map(this::toCommonPermission)
                    .collect(Collectors.toSet());
        }
        commonUser.setPermissions(commonPermissions);

        return commonUser;
    }

    private com.gygy.common.entity.Role toCommonRole(com.gygy.userservice.entity.Role role) {
        if (role == null) {
            return null;
        }

        com.gygy.common.entity.Role commonRole = new com.gygy.common.entity.Role();
        commonRole.setName(role.getName());

        Set<com.gygy.common.entity.Permission> commonPermissions = new HashSet<>();
        if (role.getPermissions() != null) {
            commonPermissions = role.getPermissions().stream()
                    .map(this::toCommonPermission)
                    .collect(Collectors.toSet());
        }
        commonRole.setPermissions(commonPermissions);

        return commonRole;
    }

    private com.gygy.common.entity.Permission toCommonPermission(com.gygy.userservice.entity.Permission permission) {
        if (permission == null) {
            return null;
        }

        com.gygy.common.entity.Permission commonPermission = new com.gygy.common.entity.Permission();
        commonPermission.setName(permission.getName());
        return commonPermission;
    }

    public User activateUser(User user) {
        user.setActive(true);
        user.setActivationToken(null);
        user.setActivationTokenExpiry(null);
        return user;
    }

    /**
     * Maps a User entity to a GetUserListDto
     * 
     * @param user The User entity to map
     * @return The mapped GetUserListDto
     */
    public GetUserListDto mapToGetUserListDto(User user) {
        if (user == null) {
            return null;
        }

        return new GetUserListDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.isActive(),
                mapRoleNames(user),
                mapPermissionNames(user));
    }

    /**
     * Maps a list of User entities to a list of GetUserListDto
     * 
     * @param users The list of User entities to map
     * @return The mapped list of GetUserListDto
     */
    public List<GetUserListDto> mapToGetUserListDtoList(List<User> users) {
        if (users == null) {
            return null;
        }

        return users.stream()
                .map(this::mapToGetUserListDto)
                .collect(Collectors.toList());
    }

    /**
     * Maps a User entity to a GetUserByIdResponse
     * 
     * @param user The User entity to map
     * @return The mapped GetUserByIdResponse
     */
    public GetUserByIdResponse mapToGetUserByIdResponse(User user) {
        if (user == null) {
            return null;
        }

        return new GetUserByIdResponse(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getBirthDate(),
                user.isActive(),
                mapRoleNames(user),
                mapPermissionNames(user));
    }

    /**
     * Extracts role names from a User entity
     * 
     * @param user The User entity
     * @return List of role names
     */
    private List<String> mapRoleNames(User user) {
        return user.getRoles() != null
                ? user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toList())
                : null;
    }

    /**
     * Extracts permission names from a User entity
     * 
     * @param user The User entity
     * @return List of permission names
     */
    private List<String> mapPermissionNames(User user) {
        return user.getPermissions() != null
                ? user.getPermissions().stream()
                        .map(permission -> permission.getName())
                        .collect(Collectors.toList())
                : null;
    }
}
