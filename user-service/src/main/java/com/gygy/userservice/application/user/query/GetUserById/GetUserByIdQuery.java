package com.gygy.userservice.application.user.query.GetUserById;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;
import an.awesome.pipelinr.Command;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.persistance.UserRepository;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.core.pipelines.authorization.RequiresAuthorization;
import com.gygy.userservice.application.user.mapper.UserMapper;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserByIdQuery implements Command<GetUserByIdResponse>, RequiresAuthorization {
    private UUID id;

    @Override
    public List<String> getRequiredAuthorizations() {
        return List.of("VIEW_USERS", "ADMIN");
    }

    @Component
    @RequiredArgsConstructor
    public static class GetUserByIdQueryHandler
            implements Command.Handler<GetUserByIdQuery, GetUserByIdResponse> {
        private final UserRepository userRepository;
        private final UserMapper userMapper;

        @Override
        public GetUserByIdResponse handle(GetUserByIdQuery query) {
            User user = userRepository.findById(query.getId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + query.getId()));

            return userMapper.mapToGetUserByIdResponse(user);
        }
    }
}