package com.marketplace.userservice.service;

public interface RabitEventPublisher {

    void publishResetPassword(String email, String link,String login);

}
