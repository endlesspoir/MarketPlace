package com.marketplace.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder,
                               @Value("${user-service.url}") String userServiceUrl) {
        return builder.routes()
                .route("user-service-auth", r -> r
                        .path("/api/auth/**")
                        .uri(userServiceUrl))
                .build();
    }
}