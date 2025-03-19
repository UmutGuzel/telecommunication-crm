package com.gygy.userservice.application.user.command.UpdateUser;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Assertions;

import com.gygy.userservice.persistance.UserRepository;
import com.gygy.userservice.application.user.rule.UserRule;
import com.gygy.userservice.application.user.mapper.UserMapper;
import com.gygy.userservice.entity.User;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UpdateUserCommandTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserRule mockUserRule;

    @Mock
    private UserMapper mockUserMapper;

    @InjectMocks
    private UpdateUserCommand.UpdateUserCommandHandler handler;

    @Test
    void givenValidCommand_whenHandleUpdateUser_thenReturnResponse() {
        // Given
        String name = "John Updated";
        String email = "john.doe@example.com";
        UpdateUserCommand command = UpdateUserCommand.builder()
                .name(name)
                .email(email)
                .build();

        User user = User.builder().email(email).build();

        when(mockUserRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        UpdateUserResponse response = handler.handle(command);

        // Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("User updated successfully", response.getMessage());

        verify(mockUserRepository).findByEmail(email);
        verify(mockUserRule).checkUserExists(user);
        verify(mockUserMapper).updateEntity(user, command);
        verify(mockUserRepository).save(user);
    }
}