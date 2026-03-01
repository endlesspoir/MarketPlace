package com.marketplace.userservice.service;

/**
 * Service abstraction for publishing events to RabbitMQ.
 *
 * Provides methods to send events related to user password management.
 */
public interface RabitEventPublisher {

    /**
     * Publishes a password reset event to the message broker.
     *
     * @param email the user's email to send the reset link
     * @param link  the password reset link or token URL
     * @param login the user's login for reference in the event
     */
    void publishResetPassword(String email, String link, String login);
}