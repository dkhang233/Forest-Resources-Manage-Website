package com.project.forestresourcesmanageapplication.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.forestresourcesmanageapplication.dtos.UserDTO;
import com.project.forestresourcesmanageapplication.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping("")
	public ResponseEntity<List<UserDTO>> retrieveAllUsers() {
		List<UserDTO> users = this.userService.retrieveAllUsers();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{username}")
	public ResponseEntity<UserDTO> retrieveUserByUsername(@PathVariable String username){
		UserDTO userDTO = this.userService.retrieveUserByUsername(username);
		return ResponseEntity.ok(userDTO);
	}

	@PostMapping("/{username}")
	public ResponseEntity<UserDTO> updateUser(@PathVariable String username,@RequestBody UserDTO userDTO) {
		userDTO = this.userService.updateUser(username,userDTO);
		return ResponseEntity.ok(userDTO);
	}

	// @PostMapping("/{username}")
	// public ResponseEntity<String> updateUser(@PathVariable String username,@RequestBody UserDTO userDTO) {
	// 	userDTO = this.userService.updateUser(username,userDTO);
	// 	return ResponseEntity.badRequest().build();
	// }


}
