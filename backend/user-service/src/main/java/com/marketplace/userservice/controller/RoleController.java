package com.marketplace.userservice.controller;

import com.marketplace.userservice.dto.RoleResponse;
import com.marketplace.userservice.dto.UpdateRoleRequest;
import com.marketplace.userservice.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(
            summary = "Get all roles",
            description = "Returns all roles available in the system",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Roles successfully retrieved",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = RoleResponse.class))
                            )
                    )
            }
    )
    @GetMapping("/roles")
    public Set<RoleResponse> getAllRoles() {
        return roleService.getAllRoles();
    }

    @Operation(
            summary = "Get roles of a user",
            description = "Returns all roles assigned to a specific user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User's roles successfully retrieved",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = RoleResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User with the specified ID not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{userId}/roles")
    public Set<RoleResponse> getUserRoles(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId) {
        return roleService.getUserRoles(userId);
    }

    @Operation(
            summary = "Update roles of a user",
            description = "Updates the roles of a specific user according to the UpdateRoleRequest",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User roles successfully updated",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = RoleResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User or role not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request format or empty roles list",
                            content = @Content
                    )
            }
    )
    @PatchMapping("/{userId}/roles")
    public Set<RoleResponse> updateUserRoles(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @Parameter(description = "List of roles to update", required = true)
            @RequestBody @Valid UpdateRoleRequest updateRoleRequest) {
        return roleService.updateUserRoles(userId, updateRoleRequest);
    }
}