package com.project.forestresourcesmanageapplication.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdministrationDTO {

	@NotBlank
	private String code;
	
	@NotBlank
	private String name;

	@NotBlank
    private String subordinateName;

	@NotBlank
	private String administrativeLevelName;
}
