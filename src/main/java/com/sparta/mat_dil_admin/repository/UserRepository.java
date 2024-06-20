package com.sparta.mat_dil_admin.repository;

import com.sparta.mat_dil_admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccountId(String name);

    Optional<User> findByEmail(String email);
}
