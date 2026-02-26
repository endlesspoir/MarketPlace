package com.marketplace.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.marketplace.userservice.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

   Optional<User> findByEmail(String email);

   Optional<User> findByLogin(String username);

   Optional<User> findByPhone(String phone);
}
