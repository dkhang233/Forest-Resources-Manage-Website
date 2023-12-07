package com.project.forestresourcesmanageapplication.responses;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FacilitiesQuantityInMoth {
    private LocalDate date;
    private List<FacilitiesQuantity> data;
}
