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
public class FacilitiesQuantityInQuarter {
    private String quarter;
    private List<FacilitiesQuantity> data;
}
