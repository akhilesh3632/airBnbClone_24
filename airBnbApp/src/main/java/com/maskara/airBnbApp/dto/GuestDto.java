package com.maskara.airBnbApp.dto;

import com.maskara.airBnbApp.modal.User;
import com.maskara.airBnbApp.modal.enums.Gender;
import lombok.Data;

@Data
public class GuestDto {
    private Long id;
    private User user;
    private String name;
    private Gender gender;
    private Integer age;
}
