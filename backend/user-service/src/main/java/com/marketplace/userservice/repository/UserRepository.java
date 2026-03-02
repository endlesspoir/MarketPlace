package com.marketplace.userservice.repository;

import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.marketplace.userservice.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findById(Long id);

   Optional<User> findByEmail(String email);

   Optional<User> findByLogin(String username);

   Optional<User> findByPhone(String phone);

    boolean existsByLoginAndIdNot(String login, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
}
