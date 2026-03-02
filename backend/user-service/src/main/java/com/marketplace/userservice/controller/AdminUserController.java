package com.marketplace.userservice.controller;

import com.marketplace.userservice.dto.AdminUserSearchRequest;
import com.marketplace.userservice.dto.UserProfileResponse;
import com.marketplace.userservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor

public class AdminUserController {

    private final AdminService adminService;

    @GetMapping
    public Page<UserProfileResponse> getAll(
            AdminUserSearchRequest filters,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return adminService.findAllForAdmin(filters, pageable);
    }
}