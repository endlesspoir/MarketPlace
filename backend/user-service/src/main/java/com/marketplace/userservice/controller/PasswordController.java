package com.marketplace.userservice.controller;

import com.marketplace.userservice.dto.ChangePasswordRequest;
import com.marketplace.userservice.dto.ForgotPasswordRequest;
import com.marketplace.userservice.service.PasswordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Slf4j
@RestController
@RequestMapping("api/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("change")
    @ResponseStatus(HttpStatus.OK)
   public void changePassword(@RequestHeader("X-User-Id") Long id,
                              @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        passwordService.changePassword(id, changePasswordRequest);
   }

   @PostMapping("forgot")
    public void forgotPassword(@Valid @RequestBody  ForgotPasswordRequest forgotPasswordRequest) {
       passwordService.forgotPassword(forgotPasswordRequest);
    }
}
