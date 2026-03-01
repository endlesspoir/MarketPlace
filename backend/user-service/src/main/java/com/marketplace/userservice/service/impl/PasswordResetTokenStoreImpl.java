package com.marketplace.userservice.service.impl;

import com.marketplace.userservice.config.PasswordResetConfig;
import com.marketplace.userservice.service.PasswordResetTokenStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;


@RequiredArgsConstructor
@Service
@Slf4j
public class PasswordResetTokenStoreImpl implements PasswordResetTokenStore {

    private final StringRedisTemplate stringRedisTemplate;

    private final PasswordResetConfig passwordResetConfig;

    @Override
    public void save(String token, Long id) {
        stringRedisTemplate.opsForValue().set(key(token),id.toString(),passwordResetConfig.getResetTokenTtl());
    }

    @Override
    public Long getUserId(String token) {
        String value = stringRedisTemplate.opsForValue().get(key(token));
        return value == null ? null : Long.parseLong(value.toString());
    }

    @Override
    public void delete(String token) {
        stringRedisTemplate.delete(key(token));
    }

    private String key(String token) {
        return "passwordReset:" + token;
    }
}
