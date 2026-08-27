package com.maskara.airBnbApp.service;

import com.maskara.airBnbApp.dto.ProfileUpdateRequestDto;
import com.maskara.airBnbApp.dto.UserDto;
import com.maskara.airBnbApp.modal.User;

public interface UserService {

    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();
}
