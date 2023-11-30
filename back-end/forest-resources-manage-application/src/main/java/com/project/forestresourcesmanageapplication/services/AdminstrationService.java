package com.project.forestresourcesmanageapplication.services;

import java.util.List;

import com.project.forestresourcesmanageapplication.dtos.AdministrationDTO;
import com.project.forestresourcesmanageapplication.models.Administration;
import com.project.forestresourcesmanageapplication.models.AdministrativeLevel;
import com.project.forestresourcesmanageapplication.responses.AdministrationHierarchyResponse;

public interface AdminstrationService {
	public List<AdministrativeLevel> retrieveAllAdministrativeLevels();

	public List<Administration> retrieveAllAdministrations();
	public List<AdministrationHierarchyResponse> retrieveAllSubAdministrations(String code);
	public Administration retrieveAdministrationByCode(String code);
	public Administration updateAdministration(String code,AdministrationDTO administrationDTO);
	public void deleteByCode(String code );
    public Administration retrieveAdministrationByName(String name);
}
