package com.marketplace.userservice.controller;

import com.marketplace.userservice.dto.SessionResponse;
import com.marketplace.userservice.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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




}
