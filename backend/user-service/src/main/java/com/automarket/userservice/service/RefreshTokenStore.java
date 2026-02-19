package com.automarket.userservice.service;

import com.automarket.userservice.dto.RefreshTokenMeta;

/**
 * Abstraction for refresh token persistence layer.
 *
 * Responsible for storing, retrieving and invalidating refresh tokens.
 * Implementation may use Redis or any other storage.
 */
public interface RefreshTokenStore {

    /**
     * Persists refresh token metadata and binds it to user device.
     *
     * @param refreshToken raw refresh token value
     * @param userId user identifier
     * @param ip client IP address
     * @param userAgent client user-agent string
     */
    void save(String refreshToken, Long userId, String ip, String userAgent);

    /**
     * Retrieves refresh token metadata by token value.
     *
     * @param refreshToken raw refresh token value
     * @return stored token metadata or null if not found
     */
    RefreshTokenMeta getByToken(String refreshToken);



    /**
     * Invalidates refresh token bound to a specific device.
     *
     * @param ip client IP address
     * @param userAgent client user-agent string
     * @param userId user identifier
     */
    void deleteByDevice(String ip, String userAgent, Long userId);


    /**
     * Invalidates refresh token bound to a specific device.
     *
     * @param userId user identifier
     */
    void deleteAllByUserIdAnd(Long userId);

}
