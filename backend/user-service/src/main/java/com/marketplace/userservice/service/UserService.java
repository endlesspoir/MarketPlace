package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.UpdateUserProfileRequest;
import com.marketplace.userservice.dto.UserProfileResponse;
import com.marketplace.userservice.dto.UserPublicProfileResponse;

public interface UserService {


    UserProfileResponse getUserProfile(Long id);

    UserProfileResponse updateUserProfile(Long id, UpdateUserProfileRequest request);

    UserPublicProfileResponse getUserPublicProfile(Long id);
}
