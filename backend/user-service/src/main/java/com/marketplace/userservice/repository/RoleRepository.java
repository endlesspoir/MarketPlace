package com.marketplace.userservice.repository;

import com.marketplace.userservice.model.Role;
import com.marketplace.userservice.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findAll();

    Optional<Role> findByName(RoleType name);

    @Query("""
    select r
    from User u
    join u.roles r
    where u.id = :id
        """)
    Set<Role> findRolesByUserId(@Param("id") Long id);




}
