package com.marketplace.gateway.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.gateway.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Global JWT authentication filter for API Gateway.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Validates access JWT token</li>
 *   <li>Checks token expiration</li>
 *   <li>Checks user roles for protected endpoints</li>
 *   <li>Extracts user data from JWT and forwards it as headers</li>
 *   <li>Ensures presence of User-Agent, X-Device-Id and client IP</li>
 * </ul>
 *
 * <p>Headers added to downstream services:
 * <ul>
 *   <li>X-User-Id</li>
 *   <li>X-User-Login</li>
 *   <li>X-User-Roles</li>
 *   <li>X-User-Ip</li>
 *   <li>X-User-Agent</li>
 * </ul>
 */

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtConfig jwtConfig;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private static final String BEARER_PREFIX = "Bearer";
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_LOGIN_HEADER = "X-User-Login";
    private static final String USER_ROLES_HEADER = "X-User-Roles";
    private static final String USER_IP_HEADER = "X-User-Ip";
    private static final String USER_AGENT_HEADER = "X-User-Agent";
    private static final String USER_DEVICE_ID ="X-Device-Id";


    private static final List<String> PUBLIC_PATHS  = List.of(
            "/api/auth/**"
    );

    private static final List<String> ADMIN_PATHS = List.of(

    );

    public JwtAuthenticationFilter(JwtConfig jwtConfig, ReactiveStringRedisTemplate reactiveStringRedisTemplate) {
        this.jwtConfig = jwtConfig;
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.debug("Incoming request: {} {} from {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI(),
                exchange.getRequest().getRemoteAddress());

        String path = exchange.getRequest().getPath().value();

        String deviceId = exchange.getRequest().getHeaders().getFirst(USER_DEVICE_ID);
        if (deviceId==null||deviceId.isBlank()) {
            log.warn("Blocked request without X-Device-Id header, path={}", path);
            return writeError(exchange, HttpStatus.BAD_REQUEST,"DeviceId header is required");
        }

        String userAgent = exchange.getRequest().getHeaders().getFirst("User-Agent");
        if (userAgent == null||userAgent.isBlank()) {
            log.warn("Blocked request without User-Agent, path={}", path);
            return writeError(exchange, HttpStatus.BAD_REQUEST,"User-Agent header is required");
        }

        if (isMatch(PUBLIC_PATHS, path)) {
            return chain.filter(exchange);
        }

        String clientIp = resolveClientIp(exchange);
        if (clientIp == null) {
            return writeError(exchange, HttpStatus.BAD_REQUEST, "Unable to determine client IP");
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null|| !authHeader.startsWith(BEARER_PREFIX)) {
            log.warn("Unauthorized request without Bearer token, path={}", path);
            return writeError(exchange, HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(BEARER_PREFIX.length()).trim();

        try{
            Claims claims = Jwts.parser()
                    .verifyWith(jwtConfig.getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String type = claims.get("type", String.class);
            if (!"access".equals(type)) {
                return writeError(exchange, HttpStatus.UNAUTHORIZED, "Refresh token is not allowed for this endpoint");
            }


            Date exp = claims.getExpiration();

            if (exp.before(new Date())) {
                return writeError(exchange, HttpStatus.UNAUTHORIZED, "Access token is expired");
            }

            List<String> roles =  claims.get("roles", List.class);

            String deviceHash = DigestUtils.sha256Hex(deviceId);
            String redisKey = "auth:ac:device:" + claims.getSubject() + ":" + deviceHash;

            return reactiveStringRedisTemplate.opsForValue()
                    .get(redisKey)
                    .flatMap(savedToken ->
                            {

                                if (savedToken == null) {
                                    log.warn("Access token not found in Redis (revoked). userId={}, deviceId={}", claims.getSubject(), deviceId);
                                    return writeError(exchange, HttpStatus.UNAUTHORIZED, "Token revoked");
                                }

                                if (!savedToken.equals(token)) {
                                    log.warn("Access token mismatch in Redis. userId={}, deviceId={}", claims.getSubject(), deviceId);
                                    return writeError(exchange, HttpStatus.UNAUTHORIZED, "Token revoked");
                                }



                                if (isMatch(ADMIN_PATHS, path) && !roles.contains("ADMIN")) {
                                    log.warn("Forbidden request. userId={}, roles={}, path={}", claims.getSubject(), roles, path);
                                    return writeError(exchange, HttpStatus.FORBIDDEN, "Access Denied");
                                }

                                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                        .header(USER_ID_HEADER, claims.getSubject())
                                        .header(USER_LOGIN_HEADER, claims.get("login", String.class))
                                        .header(USER_ROLES_HEADER, String.join(",", roles))
                                        .header(USER_IP_HEADER, clientIp)
                                        .header(USER_AGENT_HEADER, userAgent)
                                        .build();

                                log.info("Authenticated userId={}, login={}, roles={}, path={}",
                                        claims.getSubject(),
                                        claims.get("login", String.class),
                                        roles,
                                        path
                                );

                                return chain.filter(exchange.mutate().request(modifiedRequest).build());
                            }
                    );



        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT for path {}: {}", path, e.getMessage());
            return writeError(exchange, HttpStatus.UNAUTHORIZED, "Token expired");
        } catch (JwtException e) {
            log.error("Invalid JWT for path {}: {}", path, e.getMessage(), e);
            return writeError(exchange, HttpStatus.UNAUTHORIZED, "Invalid token");
        }


    }

    private boolean isMatch(List<String> patterns, String path) {
        return patterns.stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    private String resolveClientIp(ServerWebExchange exchange) {
        String xff = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
           return xff.split(",")[0].trim();
        }
        InetSocketAddress addr = exchange.getRequest().getRemoteAddress();
        return addr != null ? addr.getAddress().getHostAddress() : null;
    }

    private Mono<Void> writeError(ServerWebExchange exchange, HttpStatus status, String message)  {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> body =Map.of(
                "status",status.value(),
                "error",status.getReasonPhrase(),
                "message", message,
                "path",exchange.getRequest().getPath().value()

        );

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(body);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }catch (Exception e){
            return Mono.error(e);
        }

    }


}