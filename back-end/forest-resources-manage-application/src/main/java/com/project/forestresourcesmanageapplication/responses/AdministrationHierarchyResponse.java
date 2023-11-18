package com.project.forestresourcesmanageapplication.responses;

import java.util.ArrayList;
import java.util.List;

import com.project.forestresourcesmanageapplication.models.Administration;
import lombok.Data;

@Data
public class AdministrationHierarchyResponse {
	
	public AdministrationHierarchyResponse(Administration administration) {
		this.code = administration.getCode();
		this.name = administration.getName();
		this.fullName =  administration.getAdministrativeLevel().getName()+ " " + administration.getName();
		this.levelName = administration.getAdministrativeLevel().getName();
		this.subordinateName = administration.getSubordinate() != null ?  administration.getSubordinate().getName() : "";
		this.subordinateCode = administration.getSubordinate() != null ?  administration.getSubordinate().getCode() : "";
		this.children = new ArrayList<>();
	}
	
	private String code;
	private String name;
	private String fullName;
	private String levelName;
	private String subordinateName;
	private String subordinateCode;
	List<AdministrationHierarchyResponse> children;
}
