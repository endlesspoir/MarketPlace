package com.marketplace.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingDeviceIdException extends RuntimeException {

    public MissingDeviceIdException(String message) {
        super(message);
    }
}