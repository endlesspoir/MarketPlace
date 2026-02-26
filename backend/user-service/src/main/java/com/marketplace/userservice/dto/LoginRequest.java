package com.marketplace.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "{auth.identifier.notBlank}")
    private String identifier;

    @NotBlank(message = "{auth.password.notBlank}")
    private String password;
}