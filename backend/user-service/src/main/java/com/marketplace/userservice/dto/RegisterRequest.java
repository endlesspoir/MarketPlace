package com.marketplace.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class RegisterRequest {

    @NotBlank
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank

    private String password;

    @NotBlank
    private String login;

    @NotBlank
    private String firstName;

    private String lastName;

    @NotBlank
    @Pattern(regexp ="^\\+[1-9][0-9]{9,14}$", message = "Phone is invalid")
    private String phone;
}