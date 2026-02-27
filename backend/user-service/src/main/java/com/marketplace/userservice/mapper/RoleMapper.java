package com.marketplace.userservice.mapper;

import com.marketplace.userservice.dto.RoleResponse;
import com.marketplace.userservice.model.Role;

public class RoleMapper {

    public static RoleResponse toRoleResponse(Role role) {
        return new RoleResponse()
                .setRoleName(role.getName().toString())
                .setRoleDescription(role.getDescription())
                .setRolePermissions(role.getPermissions());

    }

}
