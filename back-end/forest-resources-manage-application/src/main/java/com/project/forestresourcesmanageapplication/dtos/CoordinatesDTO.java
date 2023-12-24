package com.project.forestresourcesmanageapplication.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoordinatesDTO {
    @NotNull
    @NotBlank
    private String code;

    @NotNull
    @NotBlank
    private String lat;
    
    @NotNull
    @NotBlank
    private String lng;
}
