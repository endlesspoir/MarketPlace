package com.marketplace.userservice.util;

import com.marketplace.userservice.exception.UserAlreadyExistsException;
import org.springframework.dao.DataIntegrityViolationException;

public class ExceptionMapper {


    public static RuntimeException mapDuplicateKeyException(DataIntegrityViolationException e) {
        String message = e.getMessage();

        if (message.contains("users_email_key")) {
            return new UserAlreadyExistsException("Email already exists");
        } else if (message.contains("users_phone_key")) {
            return new UserAlreadyExistsException("Phone already exists");
        }else if (message.contains("users_login_key")) {
            return new UserAlreadyExistsException("Login already exists");
        }
        return new UserAlreadyExistsException("User already exists");
    }
}
