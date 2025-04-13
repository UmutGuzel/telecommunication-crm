package com.gygy.userservice.application.permission.query.GetPermissionById;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;
import an.awesome.pipelinr.Command;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.persistance.PermissionRepository;
import com.gygy.userservice.entity.Permission;
import com.gygy.userservice.core.pipelines.authorization.RequiresAuthorization;
import com.gygy.userservice.application.permission.mapper.PermissionMapper;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPermissionByIdQuery implements Command<GetPermissionByIdResponse>, RequiresAuthorization {
    private UUID id;

    @Override
    public List<String> getRequiredAuthorizations() {
        return List.of("VIEW_PERMISSIONS", "ADMIN");
    }

    @Component
    @RequiredArgsConstructor
    public static class GetPermissionByIdQueryHandler
            implements Command.Handler<GetPermissionByIdQuery, GetPermissionByIdResponse> {
        private final PermissionRepository permissionRepository;
        private final PermissionMapper permissionMapper;

        @Override
        public GetPermissionByIdResponse handle(GetPermissionByIdQuery query) {
            Permission permission = permissionRepository.findById(query.getId())
                    .orElseThrow(() -> new RuntimeException("Permission not found with id: " + query.getId()));

            return permissionMapper.mapToGetPermissionByIdResponse(permission);
        }
    }
}