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
import com.project.forestresourcesmanageapplication.services.AdminstrationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/administrations")
@RequiredArgsConstructor
public class AdministrationController {
	private final AdminstrationService adminstrationService;
	
	@GetMapping("")
	public ResponseEntity<List<Administration>> retrieveAllAdministration(){
		List<Administration> administrations =  adminstrationService.retrieveAllAdministrations();
		return ResponseEntity.ok(administrations);
	}

	@GetMapping("/{code}")
	public ResponseEntity<Administration> retrieveAdministrationByCode(@PathVariable String code){
		Administration administration =  adminstrationService.retrieveAdministrationByCode(code);
		return ResponseEntity.ok(administration);
	}

	@PostMapping("/{code}")
	public ResponseEntity<Administration> updateAdministration( @PathVariable String code ,@RequestBody AdministrationDTO administrationDTO){
		Administration administration =  adminstrationService.updateAdministration(code,administrationDTO);
		return ResponseEntity.ok(administration);
	}
	
	@GetMapping("/{code}/sub")
	public ResponseEntity<List<AdministrationHierarchyResponse>> retrieveSubAdministrationsWithHierarchy(@PathVariable String code){
		List<AdministrationHierarchyResponse> administrations = adminstrationService.retrieveAllSubAdministrations(code);
		return ResponseEntity.ok(administrations);
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<Administration> retrieveAdministrationByName(@PathVariable String name){
		Administration administration =  this.adminstrationService.retrieveAdministrationByName(name);
		return ResponseEntity.ok(administration);
	}

	// @DeleteMapping("/{code}")
	// public ResponseEntity<String> deleteAdministrationByCode (@PathVariable String code){
	// 	this.administrationService.deleteByCode(code);
	// 	return ResponseEntity.ok("");
	// }
	
}
