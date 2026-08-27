package com.maskara.airBnbApp.dto;

import com.maskara.airBnbApp.modal.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileUpdateRequestDto {
    private String name;
    private Gender gender;
}
