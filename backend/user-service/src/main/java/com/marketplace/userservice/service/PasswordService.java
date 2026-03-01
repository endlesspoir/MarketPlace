package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.ChangePasswordRequest;
import com.marketplace.userservice.dto.ForgotPasswordRequest;

public interface PasswordService {

    void changePassword(Long id, ChangePasswordRequest changePasswordRequest);

    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
}
