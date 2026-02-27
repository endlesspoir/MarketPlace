package com.marketplace.userservice.service.impl;


import com.marketplace.userservice.dto.RoleResponse;
import com.marketplace.userservice.exception.UserNotFoundException;
import com.marketplace.userservice.mapper.RoleMapper;
import com.marketplace.userservice.model.User;
import com.marketplace.userservice.repository.RoleRepository;
import com.marketplace.userservice.repository.UserRepository;
import com.marketplace.userservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;


    @Override
    public List<RoleResponse> getAllRoles() {
        log.debug("getRoles called");
        return roleRepository.findAll().stream()
                .map(RoleMapper::toRoleResponse)
                .collect(Collectors.toList());


    }

    @Override
    public List<RoleResponse> getUserRoles(Long id) {
        log.debug("getUserRoles called for user with id {}", id);

        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id " + id))
                .getRoles()
                .stream()
                .map(RoleMapper::toRoleResponse)
                .collect(Collectors.toList());
    }
}
