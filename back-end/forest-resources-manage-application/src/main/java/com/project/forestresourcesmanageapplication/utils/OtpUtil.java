package com.project.forestresourcesmanageapplication.utils;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class OtpUtil {
    public String generateOtp() {
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String randomOtp = Integer.toString(randomNumber);
        while (randomOtp.length() < 6) {
            randomOtp = randomOtp.concat("0");
        }
        return randomOtp;
    }
}
