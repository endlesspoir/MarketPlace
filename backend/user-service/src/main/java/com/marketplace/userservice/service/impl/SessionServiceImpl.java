package com.marketplace.userservice.service.impl;

import com.marketplace.userservice.dto.RefreshTokenMeta;
import com.marketplace.userservice.dto.SessionResponse;
import com.marketplace.userservice.exception.SessionNotFoundException;
import com.marketplace.userservice.mapper.SessionResponseMapper;
import com.marketplace.userservice.service.AccessTokenStore;
import com.marketplace.userservice.service.RefreshTokenStore;
import com.marketplace.userservice.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final RefreshTokenStore refreshTokenStore;
    private final AccessTokenStore accessTokenStore;

    @Override
    public List<SessionResponse> getSessions(Long id) {
        List<RefreshTokenMeta> tokens = refreshTokenStore.getAll(id);

        if(tokens == null || tokens.isEmpty()) {
            log.debug("Session with id {} not found",id);
            throw new SessionNotFoundException("Session with id " + id + " not found");
        }
        return tokens.stream()
                .map(SessionResponseMapper::toSessionResponse)
                .collect(Collectors.toList());

    }

    public void deleteByDevicd(String deviceId,Long id) {
        refreshTokenStore.deleteByDevice(id, deviceId);
        accessTokenStore.delete(deviceId, id);
    }

    @Override
    public void deleteAll(Long id) {
        refreshTokenStore.deleteAllByUserIdAnd(id);
        accessTokenStore.deleteAllByUserId(id);
    }


}
