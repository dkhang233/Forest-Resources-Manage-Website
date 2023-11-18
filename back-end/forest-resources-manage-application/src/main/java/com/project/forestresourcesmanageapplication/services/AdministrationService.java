package com.project.forestresourcesmanageapplication.services;

import java.util.List;

import com.project.forestresourcesmanageapplication.models.Administration;
import com.project.forestresourcesmanageapplication.responses.AdministrationHierarchyResponse;

public interface AdministrationService {
	public List<Administration> retrieveAllAdministrations();
	public List<AdministrationHierarchyResponse> retrieveAllSubAdministrations(String code);
}
