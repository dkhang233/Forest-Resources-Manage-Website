package com.project.forestresourcesmanageapplication.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.forestresourcesmanageapplication.dtos.AdministrationDTO;
import com.project.forestresourcesmanageapplication.models.Administration;
import com.project.forestresourcesmanageapplication.responses.AdministrationHierarchyResponse;
import com.project.forestresourcesmanageapplication.services.AdministrationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/administrations")
@RequiredArgsConstructor
public class AdministrationController {
	private final AdministrationService administrationService;
	
	@GetMapping("/{code}")
	public ResponseEntity<List<AdministrationHierarchyResponse>> retrieveSubAdministrationsWithHierarchy(@PathVariable String code){
		List<AdministrationHierarchyResponse> administrations = administrationService.retrieveAllSubAdministrations(code);
		return ResponseEntity.ok(administrations);
	}

	@PostMapping("/{code}")
	public ResponseEntity<Administration> updateAdministration(@RequestBody AdministrationDTO administrationDTO){
		Administration administration =  administrationService.updateAdministration(administrationDTO);
		return ResponseEntity.ok(administration);
	}
	
}
