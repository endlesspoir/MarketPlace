package com.marketplace.userservice.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {


    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public DirectExchange passwordExchange(){
        return new DirectExchange("passwordExchange",true,false);
    }

    @Bean
    public Queue passwordResetQueue() {
        return QueueBuilder
                .durable("password.reset.queue")
                .withArgument("x-dead-letter-exchange", "password.dlx")
                .withArgument("x-dead-letter-routing-key", "password.reset.dlq")
                .withArgument("x-message-ttl", 600_000)
                .build();
    }

    @Bean
    public Queue passwordResetDlq() {
        return QueueBuilder
                .durable("password.reset.dlq")
                .build();
    }

    @Bean
    public DirectExchange passwordDlx() {
        return new DirectExchange("password.dlx", true, false);
    }

    @Bean
    public Binding passwordResetBinding() {
        return BindingBuilder
                .bind(passwordResetQueue())
                .to(passwordExchange())
                .with("password.reset");
    }

    @Bean
    public Binding passwordResetDlqBinding() {
        return BindingBuilder
                .bind(passwordResetDlq())
                .to(passwordDlx())
                .with("password.reset.dlq");
    }



}
