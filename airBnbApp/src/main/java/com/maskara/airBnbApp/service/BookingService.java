package com.maskara.airBnbApp.service;

import com.maskara.airBnbApp.dto.BookingDto;
import com.maskara.airBnbApp.dto.BookingRequest;
import com.maskara.airBnbApp.dto.GuestDto;

import java.util.List;

public interface BookingService {

    BookingDto initialiseBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);
}
