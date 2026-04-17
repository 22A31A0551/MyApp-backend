package com.example.loanbackend.repository;

import com.example.loanbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneAndPassword(String phone, String password);

    boolean existsByPhone(String phone);
}