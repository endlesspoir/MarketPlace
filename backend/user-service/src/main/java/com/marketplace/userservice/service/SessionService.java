package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.SessionResponse;

import java.util.List;

/**
 * Service abstraction for managing user sessions.
 *
 * Provides operations to retrieve active sessions and revoke sessions
 * for a specific device or across all user devices.
 */
public interface SessionService {

    /**
     * Retrieves all active sessions for the given user.
     *
     * Each session represents a device on which the user is currently authenticated.
     *
     * @param id user identifier
     * @return list of active user sessions
     * @throws com.marketplace.userservice.exception.SessionNotFoundException
     *         if no active sessions are found for the user
     */
    List<SessionResponse> getSessions(Long id);

    /**
     * Revokes (logs out) the session associated with the given device.
     *
     * This operation invalidates both:
     * <ul>
     *   <li>the refresh token bound to the device</li>
     *   <li>the access token bound to the device</li>
     * </ul>
     *
     * @param deviceId unique device identifier
     * @param id       user identifier
     */
    void deleteByDevice(String deviceId, Long id);

    /**
     * Revokes (logs out) all active sessions of the given user across all devices.
     *
     * This operation invalidates all refresh and access tokens associated with the user.
     *
     * @param id user identifier
     */
    void deleteAll(Long id);
}