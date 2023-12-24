package com.project.forestresourcesmanageapplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class verifyOtpDTO {
    private String email;
    private String otp;
}
