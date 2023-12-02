package com.project.forestresourcesmanageapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.forestresourcesmanageapplication.models.User;

public interface UserRepository extends JpaRepository<User, String> {

}

