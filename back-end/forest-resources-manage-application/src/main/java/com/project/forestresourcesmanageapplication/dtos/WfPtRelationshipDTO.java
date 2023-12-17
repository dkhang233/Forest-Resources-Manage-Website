package com.project.forestresourcesmanageapplication.dtos;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WfPtRelationshipDTO {
    @NotBlank
    private String codeWF;

    @NotBlank
    private String namePT;

    @NotBlank
    private long quantity;

    @NotBlank
    private Date date;
}
