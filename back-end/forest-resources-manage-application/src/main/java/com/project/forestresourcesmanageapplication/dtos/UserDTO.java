package com.project.forestresourcesmanageapplication.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.forestresourcesmanageapplication.models.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class UserDTO {
    private String username;

    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "last_name")
    private String lastName;

    private String email;

    private String avatar;

    private String address;

    @JsonProperty(value = "birth_date")
    // @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @JsonProperty(value = "is_active")
    private boolean isActive;

    private Role role;

    @JsonProperty("administration_name")
    private String administrationName;
}
