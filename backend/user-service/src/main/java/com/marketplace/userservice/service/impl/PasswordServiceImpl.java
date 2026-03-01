package com.marketplace.userservice.service.impl;

import com.marketplace.userservice.config.PasswordResetConfig;
import com.marketplace.userservice.dto.ChangePasswordRequest;
import com.marketplace.userservice.dto.ForgotPasswordRequest;
import com.marketplace.userservice.dto.ResetPasswordRequest;
import com.marketplace.userservice.exception.FileStorageException;
import com.marketplace.userservice.exception.InvalidCredentialsException;
import com.marketplace.userservice.exception.UserNotFoundException;
import com.marketplace.userservice.model.User;
import com.marketplace.userservice.repository.UserRepository;
import com.marketplace.userservice.service.PasswordResetTokenStore;
import com.marketplace.userservice.service.PasswordService;
import com.marketplace.userservice.service.RabitEventPublisher;
import com.marketplace.userservice.util.TokenGenerator;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordServiceImpl implements PasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RabitEventPublisher passwordRabitEventPublisher;
    private final PasswordResetTokenStore passwordResetTokenStore;
    private final PasswordResetConfig passwordResetConfig;

    @Override
    @Transactional
    public void changePassword(Long id, ChangePasswordRequest changePasswordRequest) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        log.debug("Attempting to change password for userId={}", id);

        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            log.warn("Invalid current password attempt for userId={}", id);
            throw new InvalidCredentialsException("Invalid current password");
        }

        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
            log.warn("New password same as current for userId={}", id);
            throw new InvalidCredentialsException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        log.info("Password changed successfully for userId={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        userRepository.findByEmail(forgotPasswordRequest.getEmail())
                .ifPresent(user -> {
                    String token = TokenGenerator.generateToken();

                    try {
                        passwordResetTokenStore.save(token, user.getId());
                        passwordRabitEventPublisher.publishResetPassword(
                                user.getEmail(),
                                passwordResetConfig.getFrontendUrl() + "?token=" + token,
                                user.getLogin()
                        );
                        log.info("Password reset token generated and event sent for userId={}", user.getId());
                    } catch (Exception e) {
                        log.error("Failed to process password reset for userId={}", user.getId(), e);
                        throw new FileStorageException("Failed to process password reset for user " + user.getEmail());
                    }
                });
    }


    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {

        Long id = passwordResetTokenStore.getUserId(resetPasswordRequest.getToken());

        if (id == null) {
            log.warn("Invalid password reset token used: {}", resetPasswordRequest.getToken());
            throw new InvalidCredentialsException("Invalid token");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (passwordEncoder.matches(resetPasswordRequest.getNewPassword(), user.getPassword())) {
            log.warn("New password same as current for userId={}", id);
            throw new InvalidCredentialsException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        log.info("Password reset successfully for userId={}", id);

        try {
            passwordResetTokenStore.delete(resetPasswordRequest.getToken());
            log.debug("Password reset token deleted for userId={}", id);
        } catch (Exception e) {
            log.error("Failed to delete password reset token for userId={}", id, e);
            throw new FileStorageException("Failed to process password reset for user " + user.getEmail());
        }
    }
}
