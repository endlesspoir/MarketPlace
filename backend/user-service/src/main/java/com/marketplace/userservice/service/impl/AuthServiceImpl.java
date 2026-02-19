package com.marketplace.userservice.service.impl;


import com.marketplace.userservice.dto.*;
import com.marketplace.userservice.exception.InvalidCredentialsException;
import com.marketplace.userservice.exception.UserNotFoundException;
import com.marketplace.userservice.mapper.UserMapper;
import com.marketplace.userservice.model.Role;
import com.marketplace.userservice.model.RoleType;
import com.marketplace.userservice.model.User;
import com.marketplace.userservice.repository.RoleRepository;
import com.marketplace.userservice.repository.UserRepository;
import com.marketplace.userservice.service.AuthService;
import com.marketplace.userservice.util.ExceptionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final RefreshTokenStoreImpl refreshTokenStore;

    @Override
    @Transactional
    public AuthResponse registerUser(RegisterRequest request, String ip, String userAgent) {

        Role role = roleRepository.findByName(RoleType.BUYER)
                .orElseThrow(() -> new IllegalStateException("Required role not found: " + RoleType.BUYER));

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

        refreshTokenStore.save(refreshToken, user.getId(), ip, userAgent);
        log.debug("Refresh token saved for userId={} on device [IP={}, User-Agent={}]", user.getId(), ip, userAgent);

        return new AuthResponse()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
    }

    @Override
    public AuthResponse login(LoginRequest request, String ip, String userAgent) {

        User user = findUserByIdentifier(request.getIdentifier());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Failed login attempt for userId={} from IP={}", user.getId(), ip);
            throw new InvalidCredentialsException("Wrong password");
        }

        refreshTokenStore.deleteByDevice(ip, userAgent, user.getId());
        log.debug("Old refresh token deleted for userId={} from device [IP={}, User-Agent={}]", user.getId(), ip, userAgent);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        refreshTokenStore.save(refreshToken, user.getId(), ip, userAgent);
        log.info("User logged in successfully: userId={}, ip={}", user.getId(), ip);

        return new AuthResponse()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
    }

    @Override
    public AuthResponse refreshAccessToken(RefreshTokenRequest request, String ip, String userAgent) {

        RefreshTokenMeta meta = refreshTokenStore.getByToken(request.getRefreshToken());

        if (meta == null) {
            log.warn("Invalid refresh token attempt from IP={}, User-Agent={}", ip, userAgent);
            throw new InvalidCredentialsException("Refresh token is not valid");
        }

        if (!meta.getIp().equals(ip) || !meta.getUserAgent().equals(userAgent)) {
            log.warn("Refresh token device mismatch for userId={}, IP={}, User-Agent={}", meta.getUserId(), ip, userAgent);
            throw new InvalidCredentialsException("Device info does not match");
        }

        User user = userRepository.findById(meta.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        refreshTokenStore.deleteByDevice(ip, userAgent, user.getId());
        log.debug("Old refresh token deleted for userId={} during refresh", user.getId());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        refreshTokenStore.save(refreshToken, user.getId(), ip, userAgent);
        log.info("Refresh token rotated for userId={}, IP={}", user.getId(), ip);

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
