package com.maskara.airBnbApp.service;

import com.maskara.airBnbApp.dto.HotelDto;
import com.maskara.airBnbApp.dto.HotelSearchRequest;
import com.maskara.airBnbApp.modal.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initializeRoomForAYear(Room room);


    void deleteFutureInventory(Room room);

    Page<HotelDto> searchHotels(HotelSearchRequest hotelSearchRequest);
}
