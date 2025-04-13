package com.gygy.userservice.application.role.query.GetRoleList;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;
import an.awesome.pipelinr.Command;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.persistance.RoleRepository;
import com.gygy.userservice.entity.Role;
import com.gygy.userservice.core.pipelines.authorization.RequiresAuthorization;
import com.gygy.userservice.application.role.mapper.RoleMapper;

@Getter
@Setter
@NoArgsConstructor
public class GetRoleListQuery implements Command<List<GetRoleListDto>>, RequiresAuthorization {

    @Override
    public List<String> getRequiredAuthorizations() {
        return List.of("VIEW_ROLES", "ADMIN");
    }

    @Component
    @RequiredArgsConstructor
    public static class GetRoleListQueryHandler
            implements Command.Handler<GetRoleListQuery, List<GetRoleListDto>> {
        private final RoleRepository roleRepository;
        private final RoleMapper roleMapper;

        @Override
        public List<GetRoleListDto> handle(GetRoleListQuery query) {
            List<Role> roles = roleRepository.findAll();

            return roleMapper.mapToGetRoleListDtoList(roles);
        }
    }
}