package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.ChangePasswordRequest;
import com.marketplace.userservice.dto.ForgotPasswordRequest;
import com.marketplace.userservice.dto.ResetPasswordRequest;
import com.marketplace.userservice.exception.FileStorageException;
import com.marketplace.userservice.exception.InvalidCredentialsException;
import com.marketplace.userservice.exception.UserNotFoundException;

/**
 * Service abstraction for managing user passwords.
 *
 * Provides methods to change passwords, initiate password resets,
 * and reset passwords using a token.
 */
public interface PasswordService {

    /**
     * Changes the password of a user given their ID.
     *
     * @param id the ID of the user
     * @param changePasswordRequest the request containing current and new passwords
     * @throws UserNotFoundException if the user does not exist
     * @throws InvalidCredentialsException if the current password is invalid
     */
    void changePassword(Long id, ChangePasswordRequest changePasswordRequest);

    /**
     * Initiates a password reset flow for a user.
     * Generates a reset token and sends an email or event to the user.
     *
     * @param forgotPasswordRequest the request containing the user's email
     * @throws FileStorageException if token cannot be saved or email/event fails
     */
    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    /**
     * Resets the user's password using a valid token.
     *
     * @param resetPasswordRequest the request containing the reset token and new password
     * @throws InvalidCredentialsException if the token is invalid
     * @throws UserNotFoundException if the user does not exist
     * @throws InvalidCredentialsException if the new password is the same as the current one
     * @throws FileStorageException if the token cannot be deleted
     */
    void resetPassword(ResetPasswordRequest resetPasswordRequest);
}