package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.RefreshTokenMeta;

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
     * @param deviceId client unique string
     * @param userAgent client user-agent string
     */
    void save(String refreshToken, Long userId, String ip, String userAgent,String deviceId);

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
     * @param userId user identifier
     * @param deviceId client unique string
     */
    void deleteByDevice(Long userId,String deviceId);


    /**
     * Invalidates refresh token bound to a specific device.
     *
     * @param userId user identifier
     */
    //void deleteAllByUserIdAnd(Long userId);

}
