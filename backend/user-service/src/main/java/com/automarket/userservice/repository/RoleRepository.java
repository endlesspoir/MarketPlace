package com.automarket.userservice.repository;

import com.automarket.userservice.model.Role;
import com.automarket.userservice.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleType roleType);
}
