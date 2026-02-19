package com.automarket.userservice.service;

import com.automarket.userservice.model.User;

/**
 * JWT-related operations used in authentication flow.
 * Generates access and refresh tokens and provides basic token inspection.
 */
public interface JwtService {

    /**
     * Generates short-lived access token used for API authorization.
     *
     * @param user authenticated user
     * @return signed JWT access token
     */
    String generateAccessToken(User user);

    /**
     * Generates long-lived refresh token used for access token renewal.
     *
     * @param user authenticated user
     * @return signed JWT refresh token
     */
    String generateRefreshToken(User user);


}
