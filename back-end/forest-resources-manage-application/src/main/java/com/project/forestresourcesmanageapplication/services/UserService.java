package com.project.forestresourcesmanageapplication.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.forestresourcesmanageapplication.dtos.LoginDTO;
import com.project.forestresourcesmanageapplication.dtos.UserDTO;

public interface UserService {
	List<UserDTO> retrieveAllUsers();

	UserDTO createUser(UserDTO userDTO , MultipartFile avatarFile);

	UserDTO retrieveUserByUsername(String username);

	UserDTO updateUser(String username,UserDTO userDTO , MultipartFile avatarFile);

	String login(LoginDTO loginDTO);

}
