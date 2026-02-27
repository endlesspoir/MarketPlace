package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.RoleResponse;
import com.marketplace.userservice.dto.UpdateRoleRequest;
import com.marketplace.userservice.exception.RoleNotFoundException;
import com.marketplace.userservice.exception.UserNotFoundException;

import java.util.Set;

/**
 * Service abstraction for user role management.
 *
 * Responsible for retrieving, assigning, and updating user roles.
 */
public interface RoleService {

    /**
     * Retrieves all roles available in the system.
     *
     * @return a set of RoleResponse objects representing all roles
     */
    Set<RoleResponse> getAllRoles();

    /**
     * Retrieves all roles assigned to a specific user.
     *
     * @param id the user ID
     * @return a set of RoleResponse objects representing the user's roles
     * @throws UserNotFoundException if the user with the given ID does not exist
     */
    Set<RoleResponse> getUserRoles(Long id);

    /**
     * Updates the roles of a specific user according to the provided UpdateRoleRequest.
     *
     * @param id the user ID
     * @param updateRoleRequest DTO containing the list of roles to assign
     * @return a set of RoleResponse objects representing the updated roles
     * @throws UserNotFoundException if the user with the given ID does not exist
     * @throws RoleNotFoundException if any role in the request does not exist
     */
    Set<RoleResponse> updateUserRoles(Long id, UpdateRoleRequest updateRoleRequest);
}