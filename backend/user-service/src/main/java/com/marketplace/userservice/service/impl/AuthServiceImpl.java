package com.marketplace.userservice.service.impl;


import com.marketplace.userservice.dto.*;
import com.marketplace.userservice.exception.FileStorageException;
import com.marketplace.userservice.exception.InvalidCredentialsException;
import com.marketplace.userservice.exception.RoleNotFoundException;
import com.marketplace.userservice.exception.UserNotFoundException;
import com.marketplace.userservice.mapper.UserMapper;
import com.marketplace.userservice.model.Role;
import com.marketplace.userservice.model.RoleType;
import com.marketplace.userservice.model.User;
import com.marketplace.userservice.repository.RoleRepository;
import com.marketplace.userservice.repository.UserRepository;
import com.marketplace.userservice.service.AcessTokenStore;
import com.marketplace.userservice.service.AuthService;
import com.marketplace.userservice.util.ExceptionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final RefreshTokenStoreImpl refreshTokenStore;
    private final AcessTokenStore accessTokenStore;

    @Override
    @Transactional
    public AuthResponse registerUser(RegisterRequest request, String ip, String userAgent,String deviceId) {

        Role role = roleRepository.findByName(RoleType.BUYER)
                .orElseThrow(() -> new RoleNotFoundException("Required role not found: " + RoleType.BUYER));

        User user = UserMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.getRoles().add(role);
        role.getUsers().add(user);

        try {
            userRepository.save(user);
            log.info("User registered successfully: login={}, email={}, ip={}", user.getLogin(), user.getEmail(), ip);
        } catch (DataIntegrityViolationException e) {
            log.warn("Failed to register user: login={}, email={}, ip={}, reason={}",
                    user.getLogin(), user.getEmail(), ip, e.getMessage());
            throw ExceptionMapper.mapDuplicateKeyException(e);
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        try {
            refreshTokenStore.save(refreshToken, user.getId(), ip, userAgent,deviceId);
            accessTokenStore.save(accessToken, deviceId, user.getId());
            log.debug("Refresh token saved for userId={} on device [IP={}, User-Agent={},deviceId={}]", user.getId(), ip, userAgent,deviceId);
        } catch (Exception e) {
            log.error("Failed to save refresh token in Redis for userId={}", user.getId(), e);
            throw new FileStorageException("Failed to save refresh token for user " + user.getEmail());
        }
        log.debug("Refresh token saved for userId={} on device [IP={}, User-Agent={},deviceId={}]", user.getId(), ip, userAgent,deviceId);

        return new AuthResponse()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
    }

    @Override
    public AuthResponse login(LoginRequest request, String ip, String userAgent,String deviceId) {

        User user = findUserByIdentifier(request.getIdentifier());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Failed login attempt for userId={} from IP={}", user.getId(), ip);
            throw new InvalidCredentialsException("Wrong password");
        }

        try {
            refreshTokenStore.deleteByDevice(user.getId(), deviceId);
        } catch (Exception e) {
            log.error("Failed to delete old refresh token for userId={}", user.getId(), e);
            throw new FileStorageException("Failed to delete old refresh token for user " + user.getEmail());
        }
        log.debug("Old refresh token deleted for userId={} from device [IP={}, User-Agent={}]", user.getId(), ip, userAgent);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        try {
            refreshTokenStore.save(refreshToken, user.getId(), ip, userAgent, deviceId);
            accessTokenStore.save(accessToken, deviceId, user.getId());
        } catch (Exception e) {
            log.error("Failed to save refresh token in Redis for userId={}", user.getId(), e);
            throw new FileStorageException("Failed to save refresh token for user " + user.getEmail());
        }

        log.info("User logged in successfully: userId={}, ip={},deviceId={}", user.getId(), ip,deviceId);

        return new AuthResponse()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
    }

    @Override
    public AuthResponse refreshAccessToken(RefreshTokenRequest request, String ip, String userAgent,String deviceId) {

        String refreshToken = request.getRefreshToken();

        RefreshTokenMeta meta = refreshTokenStore.getByToken(refreshToken);

        if (meta == null || meta.getExpiresAt().isBefore(Instant.now())) {
            log.warn("Invalid refresh token attempt from IP={}, User-Agent={} ,deviceId ={}", ip, userAgent,deviceId);
            throw new InvalidCredentialsException("Refresh token is not valid");
        }

        if (!meta.getDeviceId().equals(deviceId)) {
            log.warn("Refresh token device mismatch for userId={}, IP={}, User-Agent={},deviceId ={}", meta.getUserId(), ip, userAgent,deviceId);
            throw new InvalidCredentialsException("Device info does not match");
        }

        User user = userRepository.findById(meta.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));


        if(Duration.between(Instant.now(), meta.getExpiresAt()).toDays() < 1){
            log.debug("Old refresh token deleted for userId={} during refresh", user.getId());
            refreshToken = jwtService.generateRefreshToken(user);
            try {
                refreshTokenStore.save(refreshToken, user.getId(), ip, userAgent, deviceId);
            } catch (Exception e) {
                log.error("Failed to rotate refresh token for userId={}", user.getId(), e);
                throw new FileStorageException("Failed to rotate refresh token for user " + user.getEmail());
            }
            log.info("Refresh token rotated for userId={}, IP={},deviceId={}", user.getId(), ip,deviceId);
        }

        String accessToken = jwtService.generateAccessToken(user);

        try {
            accessTokenStore.save(accessToken, deviceId,user.getId());
        }
        catch (Exception e) {
            log.error("Failed to rotate acess token for userId={}", user.getId(), e);
            throw new FileStorageException("Failed to rotate acess token for user " + user.getEmail());
        }
        return new AuthResponse()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
    }

    private User findUserByIdentifier(String identifier) {
        User user;
        if (identifier.contains("@")) {
            user = userRepository.findByEmail(identifier)
                    .orElseThrow(() -> new InvalidCredentialsException("User not found by email: " + identifier));
        } else if (identifier.matches("^\\+[1-9][0-9]{9,14}$")) {
            user = userRepository.findByPhone(identifier)
                    .orElseThrow(() -> new InvalidCredentialsException("User not found by phone: " + identifier));
        } else {
            user = userRepository.findByLogin(identifier)
                    .orElseThrow(() -> new InvalidCredentialsException("User not found by login: " + identifier));
        }
        log.debug("Found user by identifier={} -> userId={}", identifier, user.getId());
        return user;
    }
}
