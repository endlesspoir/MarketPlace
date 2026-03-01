package com.marketplace.userservice.util;

import org.springframework.stereotype.Component;

import java.util.UUID;


public  class TokenGenerator {

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}