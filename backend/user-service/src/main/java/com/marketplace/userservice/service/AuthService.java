package com.marketplace.userservice.service;


import com.marketplace.userservice.dto.AuthResponse;
import com.marketplace.userservice.dto.LoginRequest;
import com.marketplace.userservice.dto.RefreshTokenRequest;
import com.marketplace.userservice.dto.RegisterRequest;

/**
 * Service for user authentication and registration.
 *
 * <p>Provides methods to register new users, authenticate existing users,
 * and refresh access tokens using refresh tokens.
 */
public interface AuthService {

    /**
     * Registers a new user with default roles and generates tokens.
     *
     * @param request registration request containing user data
     * @param ip client IP address
     * @param userAgent client User-Agent string
     * @return authentication response containing access and refresh tokens
     */
    AuthResponse registerUser(RegisterRequest request, String ip, String userAgent);

    /**
     * Authenticates a user using login credentials and issues new tokens.
     *
     * @param request login request containing identifier (email, phone, or login) and password
     * @param ip client IP address
     * @param userAgent client User-Agent string
     * @return authentication response containing access and refresh tokens
     */
    AuthResponse login(LoginRequest request, String ip, String userAgent);

    /**
     * Refreshes an access token using a valid refresh token.
     *
     * @param request refresh token request
     * @param ip client IP address
     * @param userAgent client User-Agent string
     * @return authentication response containing new access and refresh tokens
     */
    AuthResponse refreshAccessToken(RefreshTokenRequest request, String ip, String userAgent);
}
