package com.marketplace.userservice.controller;

import com.marketplace.userservice.dto.AdminUserSearchRequest;
import com.marketplace.userservice.dto.UserProfileResponse;
import com.marketplace.userservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin Users", description = "Admin user search and management")
public class AdminUserController {

    private final AdminService adminService;

    @GetMapping
    @Operation(
            summary = "Get users for admin",
            description = "Search users with filters and pagination",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Page of users",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserProfileResponse.class)
                            )
                    )
            }
    )
    public Page<UserProfileResponse> getAll(
            @ParameterObject AdminUserSearchRequest filters,
            @ParameterObject
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return adminService.findAllForAdmin(filters, pageable);
    }
}