package com.automarket.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RefreshTokenMeta {

    private Long userId;
    private Instant expiresAt;
    private String userAgent;
    private String ip;
}
