package com.project.forestresourcesmanageapplication.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.forestresourcesmanageapplication.models.User;
import com.project.forestresourcesmanageapplication.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	
	@GetMapping("")
	public ResponseEntity<List<User>> retrieveAllUsers(){
		List<User> users = userService.retrieveAllUsers();
		return ResponseEntity.ok(users);
	}
}
