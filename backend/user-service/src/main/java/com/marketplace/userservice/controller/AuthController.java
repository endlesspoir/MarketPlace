package com.marketplace.userservice.controller;


import com.marketplace.userservice.dto.AuthResponse;
import com.marketplace.userservice.dto.LoginRequest;
import com.marketplace.userservice.dto.RefreshTokenRequest;
import com.marketplace.userservice.dto.RegisterRequest;

import com.marketplace.userservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration, login and token refresh")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account and returns JWT tokens",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "409", description = "UserAlredyExist")
            }
    )
    @PostMapping("/register")
    public AuthResponse registerUser(@RequestBody @Valid RegisterRequest request,
                                     HttpServletRequest http)
    {
        String deviceId = http.getHeader("X-Device-Id");


        log.info("Registering user {}", request);

        return authService.registerUser(request,
                http.getHeader("X-Forwarded-For"),
                http.getHeader("User-Agent"),
                deviceId);
    }

    @Operation(
            summary = "User login",
            description = "Authenticates user and returns JWT tokens",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials"),
                    @ApiResponse(responseCode = "404",description = "User Not Found")
            }
    )
    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request,
                              HttpServletRequest http) {
        log.info("User login attempt {}", request);
        String deviceId = http.getHeader("X-Device-Id");

        return authService.login(request,
                http.getHeader("X-Forwarded-For"),
                http.getHeader("User-Agent"),
                deviceId);
    }

    @Operation(
            summary = "Refresh access token",
            description = "Refreshes JWT access token using refresh token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token refreshed",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid refresh token")
            }
    )
    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody @Valid RefreshTokenRequest request,
                                     HttpServletRequest http) {
        String deviceId = http.getHeader("X-Device-Id");

        log.info("Refreshing token for request: {} ", request);
        return authService.refreshAccessToken(request,
                http.getHeader("X-Forwarded-For"),
                http.getHeader("User-Agent"),
                deviceId);
    }
}
