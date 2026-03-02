package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.RefreshTokenMeta;

import java.util.List;

/**
 * Abstraction for refresh token persistence layer.
 *
 * Responsible for storing, retrieving and invalidating refresh tokens.
 * Implementations may use Redis, databases, or any other storage backend.
 */
public interface RefreshTokenStore {

    /**
     * Persists refresh token metadata and binds it to a specific user device.
     *
     * If a token already exists for the same user-device pair, it will be replaced.
     *
     * @param refreshToken raw refresh token value
     * @param userId       user identifier
     * @param ip           client IP address
     * @param userAgent   client user-agent string
     * @param deviceId    client unique device identifier
     */
    void save(String refreshToken, Long userId, String ip, String userAgent, String deviceId);

    /**
     * Retrieves refresh token metadata by raw token value.
     *
     * @param refreshToken raw refresh token value
     * @return stored token metadata or {@code null} if not found or expired
     */
    RefreshTokenMeta getByToken(String refreshToken);

    /**
     * Retrieves all active refresh token metadata entries for the given user.
     *
     * @param userId user identifier
     * @return list of active refresh token metadata entries
     */
    List<RefreshTokenMeta> getAll(Long userId);

    /**
     * Invalidates refresh token bound to a specific device for the given user.
     *
     * @param userId   user identifier
     * @param deviceId client unique device identifier
     */
    void deleteByDevice(Long userId, String deviceId);

    /**
     * Invalidates all refresh tokens associated with the given user across all devices.
     *
     * This effectively logs the user out from all active sessions.
     *
     * @param userId user identifier
     */
    void deleteAllByUserId(Long userId);


    /**
     * Checks whether there is an active refresh token (session) for the given user on a specific device.
     *
     * @param userId   user identifier
     * @param deviceId client unique device identifier
     * @return {@code true} if a refresh token exists for the specified user-device pair, {@code false} otherwise
     */
    boolean hasSession(Long userId, String deviceId);
}