package com.project.forestresourcesmanageapplication.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FacilitiesQuantity {
    private String facilitiesName;
    private long quantity;

    public FacilitiesQuantity(String facilitiesCode,long quantity){
        this.facilitiesCode = "Cơ sở "+facilitiesCode;
        this.quantity = quantity;
    }
}
