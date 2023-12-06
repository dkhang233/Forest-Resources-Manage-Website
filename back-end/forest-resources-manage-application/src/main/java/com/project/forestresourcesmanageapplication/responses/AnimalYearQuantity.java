package com.project.forestresourcesmanageapplication.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnimalYearQuantity {
    private String facilitiesName;
    private String animalName;
    private YearQuantity yearQuantity;
}
