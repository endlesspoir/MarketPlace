package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.SessionResponse;

import java.util.List;

public interface SessionService {

    List<SessionResponse> getSessions(Long id);

    void deleteByDevicd(String deviceId,Long id);

    void deleteAll(Long id);
}
