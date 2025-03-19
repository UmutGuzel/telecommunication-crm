package com.gygy.userservice.application.permission.command.CreatePermission;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.gygy.userservice.persistance.PermissionRepository;
import com.gygy.userservice.application.permission.rule.PermissionRule;
import com.gygy.userservice.application.permission.mapper.PermissionMapper;
import com.gygy.userservice.entity.Permission;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;

@ExtendWith(MockitoExtension.class)
class CreatePermissionCommandTest {

    @Mock
    private PermissionRepository mockPermissionRepository;

    @Mock
    private PermissionRule mockPermissionRule;

    @Mock
    private PermissionMapper mockPermissionMapper;

    private PermissionMapper permissionMapper = new PermissionMapper();

    @InjectMocks
    private CreatePermissionCommand.CreatePermissionCommandHandler handler;

    @Test
    void givenValidCommand_whenHandleCreatePermission_thenReturnResponse() {
        // Given
        CreatePermissionCommand command = new CreatePermissionCommand();
        command.setName("READ_PRIVILEGES");
        command.setDescription("Allows reading of privileges");

        Permission permission = permissionMapper.toEntity(command);

        when(mockPermissionRepository.findByName(command.getName())).thenReturn(Optional.empty());
        when(mockPermissionMapper.toEntity(command)).thenReturn(permission);

        // When
        CreatePermissionResponse response = handler.handle(command);

        // Then
        Assertions.assertEquals("Permission created successfully", response.getMessage());
        Assertions.assertNotNull(response);

        verify(mockPermissionRepository).findByName(command.getName());
        verify(mockPermissionRule).checkPermissionExist(null);
        verify(mockPermissionRepository).save(permission);
    }
}