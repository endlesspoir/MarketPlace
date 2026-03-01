package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.ChangePasswordRequest;
import com.marketplace.userservice.dto.ForgotPasswordRequest;
import com.marketplace.userservice.dto.ResetPasswordRequest;

public interface PasswordService {

    void changePassword(Long id, ChangePasswordRequest changePasswordRequest);

    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);
}
