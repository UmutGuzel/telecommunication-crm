package com.gygy.userservice.application.user.query.GetUserList;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
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
public class GetUserListQuery implements Command<List<GetUserListDto>>, RequiresAuthorization {
    private String searchTerm;

    @Override
    public List<String> getRequiredAuthorizations() {
        return List.of("VIEW_USERS", "ADMIN");
    }

    @Component
    @RequiredArgsConstructor
    public static class GetUserListQueryHandler
            implements Command.Handler<GetUserListQuery, List<GetUserListDto>> {
        private final UserRepository userRepository;
        private final UserMapper userMapper;

        @Override
        public List<GetUserListDto> handle(GetUserListQuery query) {
            List<User> users;

            if (query.getSearchTerm() != null && !query.getSearchTerm().isEmpty()) {
                users = userRepository.findByEmailContainingOrNameContainingOrSurnameContaining(
                        query.getSearchTerm(), query.getSearchTerm(), query.getSearchTerm());
            } else {
                users = userRepository.findAll();
            }

            return userMapper.mapToGetUserListDtoList(users);
        }
    }
}