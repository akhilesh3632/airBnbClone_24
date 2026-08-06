package com.maskara.airBnbApp.service;

import com.maskara.airBnbApp.dto.HotelDto;
import com.maskara.airBnbApp.dto.HotelInfoDto;
import com.maskara.airBnbApp.modal.Hotel;

import java.util.List;

public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id, HotelDto hotelDto);

    void deleteHotelById(Long id);

    void activateHotel(Long hotelId);

    HotelInfoDto getHotelInfoById(Long hotelId);


    List<HotelDto> getAllHotels();
}
