package com.marketplace.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
public class RegisterRequest {

    @NotBlank(message = "{user.email.notBlank}")
    @Email(message = "{user.email.invalid}")
    private String email;

    @NotBlank(message = "{user.password.notBlank}")
    @Size(min = 8, max = 64, message = "{user.password.size}")
    private String password;

    @NotBlank(message = "{user.login.notBlank}")
    @Size(min = 3, max = 20, message = "{user.login.size}")
    private String login;

    @NotBlank(message = "{user.firstName.notBlank}")
    @Size(min = 1, max = 20, message = "{user.firstName.size}")
    private String firstName;

    @Size(min = 1, max = 20, message = "{user.lastName.size}")
    private String lastName;

    @NotBlank(message = "{user.phone.notBlank}")
    @Pattern(regexp ="^\\+[1-9][0-9]{9,14}$", message = "{user.phone.invalid}")
    private String phone;
}