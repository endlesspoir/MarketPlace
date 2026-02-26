package com.marketplace.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {

    @NotBlank(message = "{auth.refreshToken.notBlank}")
    private String refreshToken;
}