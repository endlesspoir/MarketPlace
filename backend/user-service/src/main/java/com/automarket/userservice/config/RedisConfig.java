package com.automarket.userservice.config;

import com.automarket.userservice.dto.RefreshTokenMeta;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {



    @Bean(name = "refreshTokenRedisTemplate")
    public RedisTemplate<String, RefreshTokenMeta> refreshTokenMetaRedisTemplate(
            RedisConnectionFactory cf,
            ObjectMapper objectMapper)
    {
     RedisTemplate<String, RefreshTokenMeta> template = new RedisTemplate<>();
     template.setConnectionFactory(cf);
     template.setKeySerializer(new StringRedisSerializer());

     Jackson2JsonRedisSerializer<RefreshTokenMeta> serializer = new Jackson2JsonRedisSerializer<>(objectMapper,RefreshTokenMeta.class);

     template.setValueSerializer(serializer);
     template.setHashKeySerializer(new StringRedisSerializer());
     template.setHashValueSerializer(serializer);
     return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory cf) {
        return new StringRedisTemplate(cf);
    }



}
