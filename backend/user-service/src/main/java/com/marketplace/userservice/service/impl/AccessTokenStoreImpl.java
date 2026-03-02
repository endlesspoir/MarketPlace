package com.marketplace.userservice.service.impl;

import com.marketplace.userservice.config.JwtConfig;
import com.marketplace.userservice.service.AccessTokenStore;
import com.marketplace.userservice.service.PasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessTokenStoreImpl implements AccessTokenStore {

    private final StringRedisTemplate stringRedisTemplate;
    private static final String ACCESS_TOKEN_KEY = "auth:ac:device:";
    private final JwtConfig jwtConfig;

    public void save (String acessToken, String deviceId,Long id) {

        delete(deviceId,id);
        String deviceKey = ACCESS_TOKEN_KEY + id + ":" + DigestUtils.sha256Hex(deviceId);

        stringRedisTemplate.opsForValue().set(deviceKey, DigestUtils.sha256Hex(acessToken),jwtConfig.getAccessExpiration());
    }

    public void delete(String deviceId, Long id) {
        String deviceKey = ACCESS_TOKEN_KEY + id + ":" + DigestUtils.sha256Hex(deviceId);
        stringRedisTemplate.delete(deviceKey);
    }

    public void deleteAllByUserId(Long id) {
        Set<String> deviceKeys = stringRedisTemplate.keys(ACCESS_TOKEN_KEY + id + ":*");
        if (deviceKeys == null||deviceKeys.isEmpty()) {
            log.info(" No devices found for userId={} to delete", id);
            return;
        }
        for (String deviceKey : deviceKeys) {
            stringRedisTemplate.delete(deviceKey);
            log.info("Deleted device key {}", deviceKey);
        }
    }
}
