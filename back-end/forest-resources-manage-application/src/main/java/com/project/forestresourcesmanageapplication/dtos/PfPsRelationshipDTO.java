package com.project.forestresourcesmanageapplication.dtos;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PfPsRelationshipDTO {
    @NotBlank
    private String codePF;

    @NotBlank
    private String namePS;

    @NotBlank
    private long quantity;

    @NotBlank
    private Date date;
}
