package com.marketplace.userservice.service.impl;


import com.marketplace.userservice.dto.RoleResponse;
import com.marketplace.userservice.mapper.RoleMapper;
import com.marketplace.userservice.repository.RoleRepository;
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


    @Override
    public List<RoleResponse> getAllRoles() {
        log.debug("getRoles called");
        return roleRepository.findAll().stream()
                .map(RoleMapper::toRoleResponse)
                .collect(Collectors.toList());


    }
}
