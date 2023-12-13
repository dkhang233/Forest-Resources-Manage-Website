package com.project.forestresourcesmanageapplication.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalsQuantityInFacility {
    private String facilityName;
    private List<AnimalsQuantity> data;
}
