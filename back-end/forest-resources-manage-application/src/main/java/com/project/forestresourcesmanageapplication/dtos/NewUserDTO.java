package com.project.forestresourcesmanageapplication.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.forestresourcesmanageapplication.models.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserDTO {
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String avatar;

    private String address;

    // @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @JsonProperty(value = "isActive")
    private boolean isActive;

    private Role role;

    private String administrationCode;
}
