package com.marketplace.userservice.controller;

import com.marketplace.userservice.dto.ChangePasswordRequest;
import com.marketplace.userservice.dto.ForgotPasswordRequest;
import com.marketplace.userservice.service.PasswordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @RequestMapping("change")
    @ResponseStatus(HttpStatus.OK)
   public void changePassword(@RequestHeader("X-User-Id") Long id,
                              @RequestBody ChangePasswordRequest changePasswordRequest) {
        passwordService.changePassword(id, changePasswordRequest);
   }

   @RequestMapping("forgot")
    public void forgotPassword(@RequestBody  ForgotPasswordRequest forgotPasswordRequest) {


       passwordService.forgotPassword(forgotPasswordRequest);
    }
}
