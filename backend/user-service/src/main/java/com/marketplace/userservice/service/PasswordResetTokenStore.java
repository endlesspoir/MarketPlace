package com.marketplace.userservice.service;

/**
 * Service abstraction for storing and managing password reset tokens.
 *
 * Provides methods to save, retrieve, and delete tokens associated with user IDs.
 * Typically implemented using a fast key-value store (e.g., Redis).
 */
public interface PasswordResetTokenStore {

    /**
     * Saves a password reset token for a specific user.
     *
     * @param token the generated reset token
     * @param id    the ID of the user associated with the token
     * @throws RuntimeException if the token cannot be saved (e.g., Redis error)
     */
    void save(String token, Long id);

    /**
     * Retrieves the user ID associated with a password reset token.
     *
     * @param token the token to look up
     * @return the user ID if the token exists, or null if not found
     * @throws RuntimeException if the retrieval fails (e.g., Redis error)
     */
    Long getUserId(String token);

    /**
     * Deletes a password reset token.
     *
     * @param token the token to delete
     * @throws RuntimeException if the deletion fails (e.g., Redis error)
     */
    void delete(String token);
}