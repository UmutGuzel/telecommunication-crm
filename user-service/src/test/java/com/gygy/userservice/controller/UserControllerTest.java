package com.gygy.userservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import an.awesome.pipelinr.Pipeline;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;

import com.gygy.userservice.application.user.command.CreateUser.CreateUserCommand;
import com.gygy.userservice.application.user.command.CreateUser.CreateUserResponse;
import com.gygy.userservice.application.user.command.UpdateUser.UpdateUserCommand;
import com.gygy.userservice.application.user.command.UpdateUser.UpdateUserResponse;
import com.gygy.userservice.application.user.command.Login.LoginCommand;
import com.gygy.userservice.application.user.command.Login.LoginResponse;
import com.gygy.userservice.application.user.command.UpdateUserRole.UpdateUserRoleCommand;
import com.gygy.userservice.application.user.command.UpdateUserRole.UpdateUserRoleResponse;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

        private MockMvc mockMvc;

        @Mock
        private Pipeline pipeline;

        @BeforeEach
        public void setup() {
                mockMvc = MockMvcBuilders.standaloneSetup(new UserController(pipeline)).build();
        }

        @Test
        void givenValidUserData_whenCreatingUser_thenReturnSuccessMessage() throws Exception {
                // Arrange
                String requestBody = """
                                {
                                    "name": "John",
                                    "email": "john.doe@example.com",
                                    "password": "password123"
                                }""";

                CreateUserResponse response = new CreateUserResponse(UUID.randomUUID(), "User created successfully");

                when(pipeline.send(any(CreateUserCommand.class))).thenReturn(response);

                // Act & Assert
                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.message").value(response.getMessage()));

                verify(pipeline).send(any(CreateUserCommand.class));
        }

        @Test
        void givenValidUserData_whenUpdatingUser_thenReturnSuccessMessage() throws Exception {
                // Arrange
                String requestBody = """
                                {
                                    "name": "John Updated",
                                    "email": "john.doe@example.com"
                                }""";

                UpdateUserResponse response = UpdateUserResponse.builder()
                                .message("User updated successfully")
                                .build();

                when(pipeline.send(any(UpdateUserCommand.class))).thenReturn(response);

                // Act & Assert
                mockMvc.perform(put("/api/auth/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value(response.getMessage()));

                verify(pipeline).send(any(UpdateUserCommand.class));
        }

        @Test
        void givenValidLoginData_whenLoggingIn_thenReturnSuccessMessage() throws Exception {
                // Arrange
                String requestBody = """
                                {
                                    "email": "john.doe@example.com",
                                    "password": "password123"
                                }""";

                LoginResponse response = LoginResponse.builder()
                                .token("token123")
                                .userId(UUID.randomUUID())
                                .email("john.doe@example.com")
                                .build();

                when(pipeline.send(any(LoginCommand.class))).thenReturn(response);

                // Act & Assert
                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").value(response.getToken()))
                                .andExpect(jsonPath("$.userId").value(response.getUserId().toString()))
                                .andExpect(jsonPath("$.email").value(response.getEmail()));

                verify(pipeline).send(any(LoginCommand.class));
        }

        @Test
        void givenValidUserRoleData_whenUpdatingUserRole_thenReturnSuccessMessage() throws Exception {
                // Arrange
                String requestBody = """
                                {
                                    "userId": "123e4567-e89b-12d3-a456-426614174000",
                                    "role": "ADMIN"
                                }""";

                UpdateUserRoleResponse response = UpdateUserRoleResponse.builder()
                                .message("User role updated successfully")
                                .build();

                when(pipeline.send(any(UpdateUserRoleCommand.class))).thenReturn(response);

                // Act & Assert
                mockMvc.perform(put("/api/auth/role")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value(response.getMessage()));

                verify(pipeline).send(any(UpdateUserRoleCommand.class));
        }
}