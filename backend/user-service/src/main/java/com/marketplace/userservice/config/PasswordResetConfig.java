package com.marketplace.userservice.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "password.reset")
public class PasswordResetConfig {
    private String frontendUrl;

    private Duration resetTokenTtl;
}