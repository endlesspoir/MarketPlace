package com.marketplace.userservice.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class AdminUserSearchRequest {

    private String q;

    private Boolean verifiedEmail;
    private Boolean verifiedPhone;

    private String role;
}