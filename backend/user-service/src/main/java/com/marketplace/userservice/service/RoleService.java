package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.RoleResponse;
import com.marketplace.userservice.dto.UpdateRoleRequest;

import java.util.Set;

public interface RoleService {

    Set<RoleResponse> getAllRoles();

    Set<RoleResponse> getUserRoles(Long id);

    Set<RoleResponse> updateUserRoles(Long id,UpdateRoleRequest updateRoleRequest);
}
