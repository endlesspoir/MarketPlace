package com.marketplace.userservice.service;

/**
 * Storage abstraction for managing access tokens bound to user devices.
 *
 * Provides methods to save and revoke access tokens for specific user devices.
 * Implementations may use in-memory stores, Redis, databases, or other backends.
 */
public interface AccessTokenStore {

    /**
     * Saves (or replaces) an access token for the given user and device.
     *
     * If a token already exists for the specified device, it will be overwritten.
     *
     * @param accessToken the raw access token to store
     * @param deviceId    the unique device identifier
     * @param id          the user identifier
     */
    void save(String accessToken, String deviceId, Long id);

    /**
     * Deletes the access token associated with the given user and device.
     *
     * @param deviceId the unique device identifier
     * @param id       the user identifier
     */
    void delete(String deviceId, Long id);

    /**
     * Deletes all access tokens associated with the given user across all devices.
     *
     * This effectively logs the user out from all active sessions.
     *
     * @param id the user identifier
     */
    void deleteAllByUserId(Long id);


}