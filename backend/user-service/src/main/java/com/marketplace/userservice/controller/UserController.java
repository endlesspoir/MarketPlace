package com.marketplace.userservice.controller;

import com.marketplace.userservice.dto.UpdateUserProfileRequest;
import com.marketplace.userservice.dto.UserProfileResponse;
import com.marketplace.userservice.dto.UserPublicProfileResponse;
import com.marketplace.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "Endpoints for user profile management")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get current user profile",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile",
                            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @GetMapping("me")
    public UserProfileResponse getUserProfile(@RequestHeader("X-User-Id") Long id){
        return userService.getUserProfile(id);
    }

    @Operation(
            summary = "Update current user profile",
            description = "Updates profile fields. Only provided fields are updated.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Validation failed (invalid field values)"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "409", description = "Login or email already exists")
            }
    )
    @PatchMapping("me")
    public UserProfileResponse updateUserProfile(@RequestHeader("X-User-Id") Long id,
                                                 @Valid @RequestBody UpdateUserProfileRequest request){
        return userService.updateUserProfile(id, request);
    }

    @Operation(
            summary = "Get public profile of another user",
            description = "Retrieves public profile of a user. Only users with role SELLER have a public profile.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved public profile",
                            content = @Content(schema = @Schema(implementation = UserPublicProfileResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "404", description = "Public profile not available (user is not a seller)")
            }
    )
    @GetMapping()
    public UserPublicProfileResponse getUserPublicProfile(@RequestParam Long id){
        return userService.getUserPublicProfile(id);
    }

    @Operation(
            summary = "Upload or delete current user's avatar",
            description = "If a file is provided, it updates the avatar. If no file is provided, the avatar is deleted.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Avatar updated or deleted successfully",
                            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Failed to upload file to storage")
            }
    )
    @PostMapping("me/avatar")
    public UserProfileResponse uploadOrDeleteAvatar(@RequestHeader("X-User-Id") Long id,
                                                    @RequestParam(value = "file", required = false) MultipartFile file){
        return userService.updateAvatar(file, id);
    }
}