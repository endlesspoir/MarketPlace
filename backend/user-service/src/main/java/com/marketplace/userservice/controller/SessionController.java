package com.marketplace.userservice.controller;

import com.marketplace.userservice.dto.SessionResponse;
import com.marketplace.userservice.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @GetMapping
    public List<SessionResponse> getSessions(@RequestHeader("X-User-Id") Long id)
    {
        return sessionService.getSessions(id);
    }

    @DeleteMapping("/{deviceId}")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestHeader("X-User-Id") Long id,@PathVariable String deviceId) {
         sessionService.deleteByDevicd(deviceId,id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void logoutAll(@RequestHeader("X-User-Id") Long id){
        sessionService.deleteAll(id);
    }

}
