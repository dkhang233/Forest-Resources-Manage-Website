package com.project.forestresourcesmanageapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.forestresourcesmanageapplication.models.User;

public interface UserRepository extends JpaRepository<User, String>{

    @Query("UPDATE  User u SET u.isActive = :isActive WHERE u.username = :username")
    void updateUserStatusByUserName(String username, boolean isActive);

}
