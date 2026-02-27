package com.marketplace.userservice.controller;


import com.marketplace.userservice.dto.RoleResponse;
import com.marketplace.userservice.repository.RoleRepository;
import com.marketplace.userservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleResponse> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping
    public List<RoleResponse> getUserRoles(@RequestParam Long id) {
        return roleService.getUserRoles(id);
    }





}
