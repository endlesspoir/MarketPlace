package com.marketplace.userservice.service.impl;

import com.marketplace.userservice.dto.AdminUserSearchRequest;
import com.marketplace.userservice.dto.UserProfileResponse;
import com.marketplace.userservice.mapper.UserProfileMapper;
import com.marketplace.userservice.model.User;
import com.marketplace.userservice.repository.UserRepository;
import com.marketplace.userservice.repository.spec.UserSpecifications;
import com.marketplace.userservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public Page<UserProfileResponse> findAllForAdmin(AdminUserSearchRequest r, Pageable pageable) {

        Specification<User> spec = Specification
                .where(UserSpecifications.smartSearch(r.getQ()))
                .and(UserSpecifications.verifiedEmail(r.getVerifiedEmail()))
                .and(UserSpecifications.verifiedPhone(r.getVerifiedPhone()))
                .and(UserSpecifications.hasRole(r.getRole()));

        return userRepository.findAll(spec, pageable)
                .map(UserProfileMapper::toUserProfile);
    }
}
