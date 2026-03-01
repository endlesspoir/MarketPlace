package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.ChangePasswordRequest;

public interface PasswordService {

    void changePassword(Long id, ChangePasswordRequest changePasswordRequest);
}
