package com.marketplace.userservice.service.impl;

import com.marketplace.userservice.dto.PasswordResetEvent;
import com.marketplace.userservice.service.RabitEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabitEventPublisherImpl implements RabitEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishResetPassword(String email, String link,String login) {
        rabbitTemplate.convertAndSend(
                "password.exchange",
                "password.reset",
                new PasswordResetEvent().setEmail(email).setToken(link).setLogin(login)
        );
    }
}