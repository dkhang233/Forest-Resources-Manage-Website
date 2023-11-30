package com.project.forestresourcesmanageapplication.services;

import java.util.List;

import com.project.forestresourcesmanageapplication.dtos.UserDTO;

public interface UserService {
	List<UserDTO> retrieveAllUsers();

	UserDTO retrieveUserByUsername(String username);

	UserDTO updateUser(String username,UserDTO userDTO);
}
