package com.gygy.userservice.application.permission.query.GetPermissionList;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;
import an.awesome.pipelinr.Command;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.persistance.PermissionRepository;
import com.gygy.userservice.entity.Permission;
import com.gygy.userservice.core.pipelines.authorization.RequiresAuthorization;
import com.gygy.userservice.application.permission.mapper.PermissionMapper;

@Getter
@Setter
@NoArgsConstructor
public class GetPermissionListQuery implements Command<List<GetPermissionListDto>>, RequiresAuthorization {

    @Override
    public List<String> getRequiredAuthorizations() {
        return List.of("VIEW_PERMISSIONS", "ADMIN");
    }

    @Component
    @RequiredArgsConstructor
    public static class GetPermissionListQueryHandler
            implements Command.Handler<GetPermissionListQuery, List<GetPermissionListDto>> {
        private final PermissionRepository permissionRepository;
        private final PermissionMapper permissionMapper;

        @Override
        public List<GetPermissionListDto> handle(GetPermissionListQuery query) {
            List<Permission> permissions = permissionRepository.findAll();

            return permissionMapper.mapToGetPermissionListDtoList(permissions);
        }
    }
}