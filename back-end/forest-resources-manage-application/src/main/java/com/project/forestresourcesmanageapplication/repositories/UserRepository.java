package com.project.forestresourcesmanageapplication.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.forestresourcesmanageapplication.models.User;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.otp = :otp")
    Optional<User> findByOtp(String otp);

    @Query("SELECT u FROM User u WHERE u.email = :username OR u.username = :username")
    Optional<User> findByUsernameOrEmail(String username);

}
