package com.project.forestresourcesmanageapplication.dtos;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnimalStorageFacilitiesDTO {
    @NotBlank
    private String name;
    
    private Date date;
    
    @NotBlank
    private long capacity;

    @NotBlank
    private String adminstrationCode;

    private String detail;
}
