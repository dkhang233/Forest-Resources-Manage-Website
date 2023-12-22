package com.project.forestresourcesmanageapplication.dtos;

import lombok.Data;

@Data
public class PlantSeedDTO {
    private String name;

    private String type;

    private String image;

    private String soilType;

    private String mainPest;

    private int harvestingPeriod;

    private String plantSeason;
}
