package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.UpdateUserProfileRequest;
import com.marketplace.userservice.dto.UserProfileResponse;
import com.marketplace.userservice.dto.UserPublicProfileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {


    UserProfileResponse getUserProfile(Long id);

    UserProfileResponse updateUserProfile(Long id, UpdateUserProfileRequest request);

    UserPublicProfileResponse getUserPublicProfile(Long id);

    UserProfileResponse updateAvatar(MultipartFile file, Long id );
}
