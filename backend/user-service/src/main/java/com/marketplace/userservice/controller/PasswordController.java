package com.marketplace.userservice.controller;

import com.marketplace.userservice.dto.ChangePasswordRequest;
import com.marketplace.userservice.dto.ForgotPasswordRequest;
import com.marketplace.userservice.dto.ResetPasswordRequest;
import com.marketplace.userservice.service.PasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/password")
@RequiredArgsConstructor
@Tag(name = "Password Management", description = "Endpoints for password operations")
public class PasswordController {

    private final PasswordService passwordService;

    @Operation(
            summary = "Change password for logged-in user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password changed successfully",content = @Content),
                    @ApiResponse(responseCode = "400", description = "Invalid current or new password",content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found",content = @Content)
            }
    )
    @PostMapping("change")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestHeader("X-User-Id") Long id,
                               @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        passwordService.changePassword(id, changePasswordRequest);
    }

    @Operation(
            summary = "Request password reset",
            description = "Generates a reset token and sends a reset link to the user's email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password reset token sent successfully",content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found",content = @Content),
                    @ApiResponse(responseCode = "500", description = "Failed to process password reset",content = @Content)
            }
    )
    @PostMapping("forgot")
    @ResponseStatus(HttpStatus.OK)
    public void forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        passwordService.forgotPassword(forgotPasswordRequest);
    }

    @Operation(
            summary = "Reset password using token",
            description = "Allows the user to reset password using a valid reset token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password reset successfully",content = @Content),
                    @ApiResponse(responseCode = "400", description = "Invalid token or new password",content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found",content = @Content),
                    @ApiResponse(responseCode = "500", description = "Failed to delete token or process reset",content = @Content)
            }
    )
    @PostMapping("reset")
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        passwordService.resetPassword(resetPasswordRequest);
    }
}