package com.project.forestresourcesmanageapplication.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnimalSpeciesDTO {
    private String name;

    @NotBlank
    private String animalType;

    private String image;

    private String mainFood;

    private String mainDisease;

    private int longevity;
    
    @NotBlank
    private int fluctuationId;
}
