package com.marketplace.userservice.controller;


import com.marketplace.userservice.dto.UpdateUserProfileRequest;
import com.marketplace.userservice.dto.UserProfileResponse;
import com.marketplace.userservice.dto.UserPublicProfileResponse;
import com.marketplace.userservice.service.UserService;
import io.swagger.v3.oas.annotations.headers.Header;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("me")
    public UserProfileResponse getUserProfile(@RequestHeader("X-User-Id") Long id){
        return userService.getUserProfile(id);
    }

    @PatchMapping("me")
    public UserProfileResponse updateUserProfile(@RequestHeader("X-User-Id") Long id,@Valid @RequestBody UpdateUserProfileRequest request){
    return userService.updateUserProfile(id, request);
    }

    @GetMapping()
    public UserPublicProfileResponse getUserPublicProfile(@RequestParam Long id){
        return userService.getUserPublicProfile(id);
    }

}
