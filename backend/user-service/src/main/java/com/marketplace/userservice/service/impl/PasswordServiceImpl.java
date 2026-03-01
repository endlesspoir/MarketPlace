package com.marketplace.userservice.service.impl;

import com.marketplace.userservice.dto.ChangePasswordRequest;
import com.marketplace.userservice.dto.ForgotPasswordRequest;
import com.marketplace.userservice.exception.InvalidCredentialsException;
import com.marketplace.userservice.exception.UserNotFoundException;
import com.marketplace.userservice.model.User;
import com.marketplace.userservice.repository.UserRepository;
import com.marketplace.userservice.service.PasswordResetTokenStore;
import com.marketplace.userservice.service.PasswordService;
import com.marketplace.userservice.service.RabitEventPublisher;
import com.marketplace.userservice.util.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RabitEventPublisher passwordRabitEventPublisher;
    private final PasswordResetTokenStore passwordResetTokenStore;

    @Override
    @Transactional
    public void changePassword(Long id, ChangePasswordRequest changePasswordRequest) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid current password");
        }

        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
    }

    @Override
    @Transactional(readOnly = true)
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        userRepository.findByEmail(forgotPasswordRequest.getEmail())
                .ifPresent(user -> {
            String token = TokenGenerator.generateToken();

            passwordResetTokenStore.save(token, user.getId());


        });
    }
}
