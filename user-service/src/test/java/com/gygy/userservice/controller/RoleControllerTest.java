package com.gygy.userservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import an.awesome.pipelinr.Pipeline;

import com.gygy.userservice.application.role.command.CreateRole.CreateRoleCommand;
import com.gygy.userservice.application.role.command.CreateRole.CreateRoleResponse;
import com.gygy.userservice.application.role.command.UpdateRolePermission.UpdateRolePermissionCommand;
import com.gygy.userservice.application.role.command.UpdateRolePermission.UpdateRolePermissionResponse;
import com.gygy.userservice.entity.Permission;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private Pipeline pipeline;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RoleController(pipeline)).build();
    }

    @Test
    void givenValidRoleData_whenCreatingRole_thenReturnCreatedRole() throws Exception {
        // Arrange
        String requestBody = """
                {
                    "name": "ADMIN",
                    "description": "Administrator role",
                    "permissionIds": []
                }""";

        CreateRoleResponse response = CreateRoleResponse.builder()
                .id(UUID.randomUUID())
                .name("ADMIN")
                .description("Administrator role")
                .build();

        when(pipeline.send(any(CreateRoleCommand.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.getId().toString()));

        verify(pipeline).send(any(CreateRoleCommand.class));
    }

    @Test
    void givenValidPermissionData_whenUpdatingRolePermissions_thenReturnUpdatedRole() throws Exception {
        // Arrange
        String roleName = "ADMIN";
        UUID permission1Id = UUID.randomUUID();
        UUID permission2Id = UUID.randomUUID();

        String requestBody = String.format("""
                {
                    "roleName": "%s",
                    "permissionIds": ["%s", "%s"]
                }""", roleName, permission1Id, permission2Id);

        Permission permission1 = Permission.builder()
                .id(permission1Id)
                .name("CREATE_USER")
                .build();

        Permission permission2 = Permission.builder()
                .id(permission2Id)
                .name("DELETE_USER")
                .build();

        UpdateRolePermissionResponse response = UpdateRolePermissionResponse.builder()
                .id(UUID.randomUUID())
                .name(roleName)
                .permissions(Arrays.asList(permission1, permission2))
                .build();

        when(pipeline.send(any(UpdateRolePermissionCommand.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/api/v1/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(roleName))
                .andExpect(jsonPath("$.permissions").isArray())
                .andExpect(jsonPath("$.permissions.length()").value(2));

        verify(pipeline).send(any(UpdateRolePermissionCommand.class));
    }
}