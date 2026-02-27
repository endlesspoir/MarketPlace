package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.RoleResponse;

import java.util.List;

public interface RoleService {

    List<RoleResponse> getAllRoles();

    List<RoleResponse> getUserRoles(Long id);
}
