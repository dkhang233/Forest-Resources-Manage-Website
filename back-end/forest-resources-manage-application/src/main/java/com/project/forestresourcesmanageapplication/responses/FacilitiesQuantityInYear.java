package com.project.forestresourcesmanageapplication.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FacilitiesQuantityInYear {
    private int year;
    private List<FacilitiesQuantity> data;
}
