package com.marketplace.userservice.mapper;

import com.marketplace.userservice.dto.RefreshTokenMeta;
import com.marketplace.userservice.dto.SessionResponse;

public class SessionResponseMapper {

    public static SessionResponse toSessionResponse(RefreshTokenMeta refreshTokenMeta) {
        return new SessionResponse().setDeviceId(refreshTokenMeta.getDeviceId())
                .setIp(refreshTokenMeta.getIp())
                .setUserAgent(refreshTokenMeta.getUserAgent())
                .setExpiresAt(refreshTokenMeta.getExpiresAt());
    }
}
