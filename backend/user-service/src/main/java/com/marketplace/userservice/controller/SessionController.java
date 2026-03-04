package com.marketplace.userservice.controller;

import com.marketplace.userservice.dto.SessionResponse;
import com.marketplace.userservice.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/sessions")
@RequiredArgsConstructor
@Tag(name = "Sessions", description = "Endpoints for managing active user sessions and device logouts")
public class SessionController {

    private final SessionService sessionService;

    @Operation(
            summary = "Get active user sessions",
            description = "Returns a list of all active sessions (devices) for the current user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved active sessions",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SessionResponse.class)))),
                    @ApiResponse(responseCode = "404", description = "No active sessions found for the user",
                    content = @Content)
            }
    )
    @GetMapping
    public List<SessionResponse> getSessions(@RequestHeader("X-User-Id") Long id) {
        return sessionService.getSessions(id);
    }

    @Operation(
            summary = "Logout from a specific device",
            description = "Invalidates access and refresh tokens for the specified device, logging the user out from that session.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully logged out from the device"),
                    @ApiResponse(responseCode = "404", description = "Session for the specified device was not found")
            }
    )
    @DeleteMapping("/{deviceId}")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestHeader("X-User-Id") Long id, @PathVariable String deviceId) {
        sessionService.deleteByDevice(deviceId, id);
    }

    @Operation(
            summary = "Logout from all devices",
            description = "Invalidates all active access and refresh tokens, logging the user out from all devices.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully logged out from all devices")
            }
    )
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void logoutAll(@RequestHeader("X-User-Id") Long id) {
        sessionService.deleteAll(id);
    }
}