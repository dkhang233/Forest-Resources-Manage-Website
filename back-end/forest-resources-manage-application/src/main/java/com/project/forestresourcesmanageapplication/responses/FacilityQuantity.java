package com.project.forestresourcesmanageapplication.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FacilityQuantity {
    private String facilityName;
    private String objName;
    private long quantity;
}
