package com.marketplace.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PasswordResetEvent {

    private String email;

    private String login;

    private String token;
}
