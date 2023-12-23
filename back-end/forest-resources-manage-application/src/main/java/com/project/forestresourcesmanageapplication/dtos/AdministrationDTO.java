package com.project.forestresourcesmanageapplication.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdministrationDTO {

	@NotBlank
	private String name;

	@NotBlank
    private String subordinateCode;

	@NotBlank
	private String administrativeLevelName;
}
