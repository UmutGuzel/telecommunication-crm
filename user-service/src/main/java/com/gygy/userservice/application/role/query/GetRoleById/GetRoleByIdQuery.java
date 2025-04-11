package com.gygy.userservice.application.role.query.GetRoleById;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;
import an.awesome.pipelinr.Command;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.persistance.RoleRepository;
import com.gygy.userservice.entity.Role;
import com.gygy.userservice.core.pipelines.authorization.RequiresAuthorization;
import com.gygy.userservice.application.role.mapper.RoleMapper;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetRoleByIdQuery implements Command<GetRoleByIdResponse>, RequiresAuthorization {
    private UUID id;

    @Override
    public List<String> getRequiredAuthorizations() {
        return List.of("VIEW_ROLES", "ADMIN");
    }

    @Component
    @RequiredArgsConstructor
    public static class GetRoleByIdQueryHandler
            implements Command.Handler<GetRoleByIdQuery, GetRoleByIdResponse> {
        private final RoleRepository roleRepository;
        private final RoleMapper roleMapper;

        @Override
        public GetRoleByIdResponse handle(GetRoleByIdQuery query) {
            Role role = roleRepository.findById(query.getId())
                    .orElseThrow(() -> new RuntimeException("Role not found with id: " + query.getId()));

            return roleMapper.mapToGetRoleByIdResponse(role);
        }
    }
}