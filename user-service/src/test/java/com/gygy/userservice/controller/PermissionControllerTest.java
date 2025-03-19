package com.gygy.userservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import an.awesome.pipelinr.Pipeline;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.Mock;

import com.gygy.userservice.application.permission.command.CreatePermission.CreatePermissionCommand;
import com.gygy.userservice.application.permission.command.CreatePermission.CreatePermissionResponse;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class PermissionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private Pipeline pipeline;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PermissionController(pipeline)).build();
    }

    @Test
    void givenValidPermissionData_whenCreatingPermission_thenReturnCreatedPermission() throws Exception {
        // Arrange
        String requestBody = """
                {
                    "name": "READ_PRIVILEGES",
                    "description": "Allows reading of privileges"
                }""";

        CreatePermissionResponse response = CreatePermissionResponse.builder()
                .message("Permission created successfully")
                .build();

        when(pipeline.send(any(CreatePermissionCommand.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/permission")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(response.getMessage()));

        verify(pipeline).send(any(CreatePermissionCommand.class));
    }
}