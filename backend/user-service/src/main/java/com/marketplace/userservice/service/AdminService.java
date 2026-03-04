package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.AdminUserSearchRequest;
import com.marketplace.userservice.dto.UserProfileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service abstraction for administrative user management and search.
 *
 * Provides methods for retrieving users for admin purposes with
 * filtering, searching, and pagination support.
 *
 * Implementations may apply business rules, security checks,
 * and compose dynamic query specifications.
 */
public interface AdminService {

    /**
     * Returns a paginated list of user profiles for admin view.
     *
     * Supports flexible filtering by multiple criteria such as:
     * search query, verification status, blocking status, and user role.
     *
     * Pagination parameters control the page number, size, and sorting.
     *
     * @param request  filter and search parameters for admin user lookup
     * @param pageable pagination and sorting information
     * @return a page of {@link UserProfileResponse} matching the given filters
     */
    Page<UserProfileResponse> findAllForAdmin(AdminUserSearchRequest request, Pageable pageable);
}