package com.marketplace.userservice.service.impl;

import com.marketplace.userservice.dto.UpdateUserProfileRequest;
import com.marketplace.userservice.dto.UserProfileResponse;
import com.marketplace.userservice.dto.UserPublicProfileResponse;
import com.marketplace.userservice.exception.UserAlreadyExistsException;
import com.marketplace.userservice.exception.UserNotFoundException;
import com.marketplace.userservice.exception.UserNotFoundPublicProfile;
import com.marketplace.userservice.mapper.UserProfileMapper;
import com.marketplace.userservice.model.RoleType;
import com.marketplace.userservice.model.User;
import com.marketplace.userservice.repository.UserRepository;
import com.marketplace.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    @Override
    public UserProfileResponse getUserProfile(Long id) {
        log.debug("Stage - getUserProfileById : {}",id);
        return UserProfileMapper.
                toUserProfile(userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("User not found with id " +id)));
    }

    @Override
    @Transactional
    public UserProfileResponse updateUserProfile(Long id, UpdateUserProfileRequest request) {
        log.debug("Stage - start updateUserProfile : {},{}",id,request);

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

        if (request.getEmail() != null &&
                userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        if (request.getLogin() != null &&
                userRepository.existsByLoginAndIdNot(request.getLogin(), id)) {
            throw new UserAlreadyExistsException(request.getLogin());
        }

        patchUser(user,request);

        log.debug("Stage - finish updateUserProfile : {}",user);

        return UserProfileMapper.toUserProfile(user);
    }

    @Override
    public UserPublicProfileResponse getUserPublicProfile(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundPublicProfile("Profile not found with id " + id));

        log.debug("Stage - start getUserPublicProfile : {}",user);

        user.getRoles().stream()
                .filter(role ->role.getName() == RoleType.SELLER)
                .findAny()
                .orElseThrow(() -> new UserNotFoundPublicProfile("Profile not found with id " + id));

        log.debug("Stage - finish getUserPublicProfile : {}",user);

        return UserProfileMapper.toUserPublicProfile(user);
    }


    private void patchUser(User user, UpdateUserProfileRequest request) {
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getLogin() != null) user.setLogin(request.getLogin());
        if (request.getCity() != null) user.getProfile().setCity(request.getCity());
        if (request.getCountry() != null) user.getProfile().setCountry(request.getCountry());
        if (request.getBio() != null) user.getProfile().setBio(request.getBio());
    }
}
