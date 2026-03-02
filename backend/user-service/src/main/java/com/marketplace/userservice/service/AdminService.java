package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.AdminUserSearchRequest;
import com.marketplace.userservice.dto.UserProfileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface AdminService {

    Page<UserProfileResponse> findAllForAdmin(AdminUserSearchRequest request, Pageable pageable);
}
