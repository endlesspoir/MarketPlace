package com.marketplace.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "{password.reset.token.notBlank}")
    private String token;

    @NotBlank(message = "{user.password.notBlank}")
    @Size(min = 8, max = 64, message = "{user.password.size}")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,64}$",
            message = "{user.password.strong}"
    )
    private String newPassword;
}