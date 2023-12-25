package com.project.forestresourcesmanageapplication.dtos;

import java.sql.Date;

import lombok.Data;

@Data
public class PlantFacilitiesDTO {
    private String name;

    private Date date;

    private long capacity;

    private String administrationCode;
}
