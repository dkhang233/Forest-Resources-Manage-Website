package com.project.forestresourcesmanageapplication.dtos;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AsfAsRelationshipDTO {
    @NotBlank
    private long quantity;

    @NotBlank
    private Date date;
}
