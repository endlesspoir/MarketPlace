package com.automarket.userservice.service.impl;

import com.automarket.userservice.config.JwtConfig;
import com.automarket.userservice.dto.RefreshTokenMeta;
import com.automarket.userservice.service.RefreshTokenStore;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;




@Service
@RequiredArgsConstructor
public class RefreshTokenStoreImpl implements RefreshTokenStore {

    private final JwtConfig jwtConfig;

    @Qualifier("refreshTokenRedisTemplate")
    private final RedisTemplate<String, RefreshTokenMeta> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    private static final String TOKEN_KEY_PREFIX = "auth:rt:token:";
    private static final String DEVICE_KEY_PREFIX = "auth:rt:device:";



    public void save (String refreshToken, Long userId,String ip, String userAgent) {

       String tokenHash = DigestUtils.sha256Hex(refreshToken);
       String deviceHash = DigestUtils.sha256Hex(userAgent + "|" + ip);


        String tokenKey = TOKEN_KEY_PREFIX +tokenHash;
        String deviceKey = DEVICE_KEY_PREFIX + userId + ":" + deviceHash;



        RefreshTokenMeta refreshTokenMeta = new RefreshTokenMeta()
                .setUserId(userId)
                .setExpiresAt(Instant.now().plus(jwtConfig.getRefreshExpiration()));

        redisTemplate.opsForValue().set(tokenKey, refreshTokenMeta,jwtConfig.getRefreshExpiration());
        stringRedisTemplate.opsForValue().set(deviceKey, tokenHash,jwtConfig.getRefreshExpiration());
    }

    public RefreshTokenMeta getByToken(String RefreshToken) {
      return redisTemplate.opsForValue().get(TOKEN_KEY_PREFIX + DigestUtils.sha256Hex(RefreshToken));
    }

    public void deleteByToken(String RefreshToken) {
        redisTemplate.delete(TOKEN_KEY_PREFIX + DigestUtils.sha256Hex(RefreshToken));
    }

    public String getByDevice(String ip, String userAgent,Long userId) {
        String deviceHash = DigestUtils.sha256Hex(userAgent + "|" + ip);
        return stringRedisTemplate.opsForValue().get(DEVICE_KEY_PREFIX + userId + ":" + deviceHash);
    }

    public void deleteByDevice(String ip, String userAgent,Long userId) {
        String deviceHash = DigestUtils.sha256Hex(userAgent + "|" + ip);
        redisTemplate.delete(DEVICE_KEY_PREFIX + userId + ":" + deviceHash);
    }

}
