package com.gygy.userservice.application.user.command.CreateUser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;

import com.gygy.userservice.persistance.UserRepository;
import com.gygy.userservice.application.user.rule.UserRule;
import com.gygy.userservice.application.user.mapper.UserMapper;
import com.gygy.userservice.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CreateUserCommandTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserRule mockUserRule;

    @Mock
    private UserMapper mockUserMapper;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @InjectMocks
    private CreateUserCommand.CreateUserCommandHandler handler;

    private UserMapper userMapper = new UserMapper();

    @BeforeEach
    void setUp() {
        handler = new CreateUserCommand.CreateUserCommandHandler(mockUserRepository, mockUserRule, mockUserMapper,
                mockPasswordEncoder);
    }

    @Test
    void givenValidCommand_whenHandleCreateUser_thenReturnResponse() {
        // Given
        String name = "John";
        String surname = "Doe";
        String email = "john.doe@example.com";
        String password = "Password1!";
        String phoneNumber = "1234567890";
        String address = "123 Main St";

        CreateUserCommand command = CreateUserCommand.builder()
                .name(name)
                .surname(surname)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .address(address).build();

        User user = userMapper.toEntity(command);

        when(mockUserRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(mockPasswordEncoder.encode(password)).thenReturn("encodedPassword");
        when(mockUserMapper.toEntity(command)).thenReturn(user);

        // When
        CreateUserResponse response = handler.handle(command);

        // Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("User created successfully", response.getMessage());

        verify(mockUserRepository).findByEmail(email);
        verify(mockUserRepository).save(user);
    }
}