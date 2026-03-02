package com.marketplace.userservice.service.impl;


import com.marketplace.userservice.dto.RoleResponse;
import com.marketplace.userservice.dto.UpdateRoleRequest;
import com.marketplace.userservice.exception.RoleNotFoundException;
import com.marketplace.userservice.exception.UserNotFoundException;
import com.marketplace.userservice.mapper.RoleMapper;
import com.marketplace.userservice.model.Role;
import com.marketplace.userservice.model.RoleType;
import com.marketplace.userservice.model.User;
import com.marketplace.userservice.repository.RoleRepository;
import com.marketplace.userservice.repository.UserRepository;
import com.marketplace.userservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;



    @Override
    public Set<RoleResponse> getAllRoles() {

        log.debug("getRoles called");

        return roleRepository.findAll().stream()
                .map(RoleMapper::toRoleResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<RoleResponse> getUserRoles(Long id) {

        log.debug("getUserRoles called for user with id {}", id);

        return roleRepository.findRolesByUserId(id)
                .stream()
                .map(RoleMapper::toRoleResponse)
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public Set<RoleResponse> updateUserRoles(Long id,UpdateRoleRequest updateRoleRequest) {

        log.debug("updateUserRoles called with updateRoleRequest {}", updateRoleRequest);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "User with id " +id + " not found"));

        Set<Role> roles = updateRoleRequest.getRoles()
                .stream()
                .map(roleReq -> roleRepository.findByName(RoleType.valueOf(roleReq.getRoleName()))
                        .orElseThrow(() -> new RoleNotFoundException(
                                "Role with name " + roleReq.getRoleName() + " not found")))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        log.debug("updateUserRoles finish with updateRoleRequest {}", updateRoleRequest);
       userRepository.save(user);

        return user.getRoles().stream()
                .map(RoleMapper::toRoleResponse)
                .collect(Collectors.toSet());
    }
}
