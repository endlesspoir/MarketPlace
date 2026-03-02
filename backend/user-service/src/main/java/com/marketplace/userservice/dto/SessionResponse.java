package com.marketplace.userservice.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
public class SessionResponse {

    private String userAgent;

    private String ip;

    private String deviceId;

    private Instant expiresAt;

}
