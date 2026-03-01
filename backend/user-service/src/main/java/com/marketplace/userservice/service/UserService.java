package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.UpdateUserProfileRequest;
import com.marketplace.userservice.dto.UserProfileResponse;
import com.marketplace.userservice.dto.UserPublicProfileResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service abstraction for user profile management.
 *
 * Provides operations to retrieve and update user profiles, including avatar management.
 */
public interface UserService {

    /**
     * Retrieves the current user's profile by their ID.
     *
     * @param id the user ID
     * @return {@link UserProfileResponse} containing profile details
     * @throws com.marketplace.userservice.exception.UserNotFoundException if the user does not exist
     */
    UserProfileResponse getUserProfile(Long id);

    /**
     * Updates the current user's profile with the given request data.
     *
     * @param id      the user ID
     * @param request {@link UpdateUserProfileRequest} containing profile updates
     * @return {@link UserProfileResponse} with the updated profile
     * @throws com.marketplace.userservice.exception.UserNotFoundException if the user does not exist
     * @throws com.marketplace.userservice.exception.UserAlreadyExistsException if login or email is already taken
     */
    UserProfileResponse updateUserProfile(Long id, UpdateUserProfileRequest request);

    /**
     * Retrieves the public profile of another user.
     *
     * @param id the user ID
     * @return {@link UserPublicProfileResponse} containing public profile information
     * @throws com.marketplace.userservice.exception.UserNotFoundPublicProfile if the public profile is not found
     */
    UserPublicProfileResponse getUserPublicProfile(Long id);

    /**
     * Updates or deletes the avatar of the current user.
     *
     * If the {@code file} is provided and not empty, it replaces the existing avatar.
     * If {@code file} is null or empty, it deletes the current avatar.
     *
     * @param file the new avatar file (optional)
     * @param id   the user ID
     * @return {@link UserProfileResponse} with the updated avatar URL
     * @throws com.marketplace.userservice.exception.UserNotFoundException if the user does not exist
     * @throws com.marketplace.userservice.exception.FileStorageException if the file cannot be uploaded/deleted
     */
    UserProfileResponse updateAvatar(MultipartFile file, Long id);
}