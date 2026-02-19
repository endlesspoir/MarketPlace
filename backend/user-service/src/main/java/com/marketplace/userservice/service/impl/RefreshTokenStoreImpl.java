package com.marketplace.userservice.service.impl;

import com.marketplace.userservice.config.JwtConfig;
import com.marketplace.userservice.dto.RefreshTokenMeta;
import com.marketplace.userservice.service.RefreshTokenStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenStoreImpl implements RefreshTokenStore {

    private final JwtConfig jwtConfig;

    @Qualifier("refreshTokenRedisTemplate")
    private final RedisTemplate<String, RefreshTokenMeta> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    private static final String TOKEN_KEY_PREFIX = "auth:rt:token:";
    private static final String DEVICE_KEY_PREFIX = "auth:rt:device:";

    public void save(String refreshToken, Long userId, String ip, String userAgent,String deviceId) {
        String deviceHash = DigestUtils.sha256Hex(deviceId);

        deleteByDevice(userId,deviceId);

        String tokenHash = DigestUtils.sha256Hex(refreshToken);
        String tokenKey = TOKEN_KEY_PREFIX + tokenHash;
        String deviceKey = DEVICE_KEY_PREFIX + userId + ":" + deviceHash;

        RefreshTokenMeta refreshTokenMeta = new RefreshTokenMeta()
                .setUserId(userId)
                .setExpiresAt(Instant.now().plus(jwtConfig.getRefreshExpiration()))
                .setIp(ip)
                .setUserAgent(userAgent)
                .setDeviceId(deviceId);

        redisTemplate.opsForValue().set(tokenKey, refreshTokenMeta, jwtConfig.getRefreshExpiration());
        stringRedisTemplate.opsForValue().set(deviceKey, tokenHash, jwtConfig.getRefreshExpiration());

        log.info("Saved refresh token for userId={} on device [IP={}, User-Agent={},deviceId={}]", userId, ip, userAgent, deviceId);
    }

    public RefreshTokenMeta getByToken(String refreshToken) {
        RefreshTokenMeta meta = redisTemplate.opsForValue().get(TOKEN_KEY_PREFIX + DigestUtils.sha256Hex(refreshToken));
        log.debug("Retrieved refresh token meta: {}", meta);
        return meta;
    }

    public void deleteByDevice( Long userId,String deviceId) {
        String deviceHash = DigestUtils.sha256Hex(deviceId);
        String tokenHash = stringRedisTemplate.opsForValue().get(DEVICE_KEY_PREFIX + userId + ":" + deviceHash);

        if (tokenHash != null) {
            redisTemplate.delete(TOKEN_KEY_PREFIX + tokenHash);
            log.debug("Deleted refresh token for userId={} on device deviceId={}", userId,deviceId);
        }
        stringRedisTemplate.delete(DEVICE_KEY_PREFIX + userId + ":" + deviceHash);
        log.info("Deleted device entry for userId={} , deviceId={}", userId,deviceId);
    }

//    public void deleteAllByUserIdAnd(Long userId) {
//        Set<String> deviceKeys = stringRedisTemplate.keys(DEVICE_KEY_PREFIX + userId + ":*");
//        if (deviceKeys == null || deviceKeys.isEmpty()) {
//            log.info("No devices found for userId={} to delete", userId);
//            return;
//        }
//
//        for (String deviceKey : deviceKeys) {
//            String tokenHash = stringRedisTemplate.opsForValue().get(deviceKey);
//            if (tokenHash != null) {
//                redisTemplate.delete(TOKEN_KEY_PREFIX + tokenHash);
//                log.debug("Deleted refresh token {} for userId={}", tokenHash, userId);
//            }
//            stringRedisTemplate.delete(deviceKey);
//            log.debug("Deleted device key {}", deviceKey);
//        }
//        log.info("Deleted all refresh tokens for userId={}", userId);
//    }
}
