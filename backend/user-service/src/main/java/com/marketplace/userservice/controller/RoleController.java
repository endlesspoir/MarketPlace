package com.marketplace.userservice.controller;


import com.marketplace.userservice.dto.RoleResponse;
import com.marketplace.userservice.dto.UpdateRoleRequest;
import com.marketplace.userservice.model.Role;
import com.marketplace.userservice.repository.RoleRepository;
import com.marketplace.userservice.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.Set;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;


    @GetMapping("/roles")
    public Set<RoleResponse> getAllRoles() {
        return roleService.getAllRoles();
    }


    @GetMapping("/users/{userId}/roles")
    public Set<RoleResponse> getUserRoles(@PathVariable Long userId) {
        return roleService.getUserRoles(userId);
    }


    @PatchMapping("/users/{userId}/roles")
    public Set<RoleResponse> updateUserRoles(@PathVariable Long userId,
                                             @RequestBody @Valid UpdateRoleRequest updateRoleRequest) {
        return roleService.updateUserRoles(userId, updateRoleRequest);
    }
}