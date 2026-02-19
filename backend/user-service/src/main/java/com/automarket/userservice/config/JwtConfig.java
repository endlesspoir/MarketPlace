package com.automarket.userservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Configuration;

import java.time.Duration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {


    private String accessSecret;

    private String refreshSecret;

    private Duration accessExpiration;

    private Duration refreshExpiration;
}
