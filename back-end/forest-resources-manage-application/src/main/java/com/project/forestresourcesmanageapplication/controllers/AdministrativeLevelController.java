package com.project.forestresourcesmanageapplication.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.forestresourcesmanageapplication.models.AdministrativeLevel;
import com.project.forestresourcesmanageapplication.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/administrativelevels")
@RequiredArgsConstructor
public class AdministrativeLevelController {
	private final UserService userService;
	
	@GetMapping("")
	public ResponseEntity<List<AdministrativeLevel>> retrieveAllAdministratives(){
		List<AdministrativeLevel> administrativelevels = userService.retrieveAllAdministrativeLevels();
		return ResponseEntity.ok(administrativelevels);
	}
}
