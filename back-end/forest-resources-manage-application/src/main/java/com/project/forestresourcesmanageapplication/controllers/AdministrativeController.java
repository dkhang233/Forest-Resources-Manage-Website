package com.project.forestresourcesmanageapplication.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.forestresourcesmanageapplication.models.Administrative;
import com.project.forestresourcesmanageapplication.services.AdministrativeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/administratives")
@RequiredArgsConstructor
public class AdministrativeController {
	private final AdministrativeService administrativeService;
	
	@GetMapping("")
	public ResponseEntity<List<Administrative>> retrieveAllAdministratives(){
		List<Administrative> administratives = administrativeService.retrieveAllAdministratives();
		return ResponseEntity.ok(administratives);
	}
}
