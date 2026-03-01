package com.marketplace.userservice.service;

public interface PasswordResetTokenStore {

    void save(String token, Long id);

    Long getUserId(String token);

    void delete(String token);

}
