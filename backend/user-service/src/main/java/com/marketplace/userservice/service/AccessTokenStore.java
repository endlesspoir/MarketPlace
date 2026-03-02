package com.marketplace.userservice.service;


public interface AccessTokenStore {

    void save(String accessToken, String deviceId,Long id);

    void delete (String deviceId,Long id);
}
