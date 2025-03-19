package com.gygy.userservice.application.user.command.UpdateUserRole;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Assertions;

import com.gygy.userservice.persistance.UserRepository;
import com.gygy.userservice.application.role.service.RoleService;
import com.gygy.userservice.application.user.rule.UserRule;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.entity.Role;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class UpdateUserRoleCommandTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private RoleService mockRoleService;

    @Mock
    private UserRule mockUserRule;

    @InjectMocks
    private UpdateUserRoleCommand.UpdateUserRoleCommandHandler handler;

    @Test
    void givenValidCommand_whenHandleUpdateUserRole_thenReturnResponse() {
        // Given
        String email = "john.doe@example.com";
        List<String> roleIds = Arrays.asList("ROLE_ADMIN", "ROLE_USER");
        UpdateUserRoleCommand command = new UpdateUserRoleCommand(email, roleIds);

        User user = User.builder().email(email).build();
        Role role1 = Role.builder().name("ROLE_ADMIN").build();
        Role role2 = Role.builder().name("ROLE_USER").build();
        List<Role> roles = Arrays.asList(role1, role2);

        when(mockUserRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(mockRoleService.getRolesByNames(roleIds)).thenReturn(roles);

        // When
        UpdateUserRoleResponse response = handler.handle(command);

        // Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("User roles updated successfully", response.getMessage());

        verify(mockUserRepository).findByEmail(email);
        verify(mockUserRule).checkUserExists(user);
        verify(mockRoleService).getRolesByNames(roleIds);
        verify(mockUserRepository).save(user);
    }
}