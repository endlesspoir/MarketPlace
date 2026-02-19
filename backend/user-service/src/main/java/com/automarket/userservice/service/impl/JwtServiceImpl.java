package com.automarket.userservice.service.impl;

import com.automarket.userservice.config.JwtConfig;
import com.automarket.userservice.model.User;
import com.automarket.userservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtConfig jwtConfig;
    private SecretKey accessKey;
    private SecretKey refreshKey;


    @PostConstruct
    public void init() {
        this.accessKey = Keys.hmacShaKeyFor(jwtConfig.getAccessSecret().getBytes(StandardCharsets.UTF_8));
        this.refreshKey = Keys.hmacShaKeyFor(jwtConfig.getRefreshSecret().getBytes(StandardCharsets.UTF_8));

        log.info("Jwt initialized.Acess TTL={}min, Refresh TTL={}day" ,
                jwtConfig.getAccessExpiration().toMinutes(),
                jwtConfig.getRefreshExpiration().toDays());
    }

    private String generateToken(User user,long ttlMs,String type,SecretKey secret) {


        Instant now = Instant.now();

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("type",type)
                .claim("login",user.getLogin())
                .claim("roles",user.getRoles().stream().map(r->r.getName().name()).toList())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(ttlMs)))
                .signWith(secret, Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return generateToken(user,jwtConfig.getRefreshExpiration().toMillis(),"refresh",refreshKey);
    }

    public String generateAccessToken(User user) {
        return generateToken(user,jwtConfig.getAccessExpiration().toMillis(),"access",accessKey);
    }

    public String getTokenType(String token,boolean isRefreshToken) {

        SecretKey key = isRefreshToken?refreshKey:accessKey;


        JwtParser parser = Jwts.parser()
                .setSigningKey(key)
                .build();

        Jws<Claims> claimsJws = parser.parseClaimsJws(token);

        String type = claimsJws.getBody().get("type", String.class);


        return type;
    }



}
