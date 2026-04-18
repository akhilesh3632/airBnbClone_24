package com.maskara.airBnbApp.service;

import com.maskara.airBnbApp.dto.HotelDto;
import com.maskara.airBnbApp.dto.HotelInfoDto;
import com.maskara.airBnbApp.dto.RoomDto;
import com.maskara.airBnbApp.exception.ResourceNotFoundException;
import com.maskara.airBnbApp.modal.Hotel;
import com.maskara.airBnbApp.modal.Room;
import com.maskara.airBnbApp.repository.HotelRepository;
import com.maskara.airBnbApp.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating new Hotel with name: {}",hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto,Hotel.class);
        hotel.setActive(false);
        hotel = hotelRepository.save(hotel);
        log.info("Created new Hotel with id: {}",hotelDto.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting  the  hotel with  Id: {}",id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with id: "+id));
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Updating  the  hotel with  Id: {}",id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with id: "+id));
        modelMapper.map(hotelDto, hotel);
        hotel.setId(id);
        hotel=hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with Id: "+id));
        hotelRepository.deleteById(id);
        for(Room room: hotel.getRooms()){
            inventoryService.deleteFutureInventory(room);
            roomRepository.deleteById(room.getId());

        }
        hotelRepository.deleteById(id);



    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("Activating  the  hotel with  Id: {}",hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with id: "+hotelId));
        hotel.setActive(true);

        for(Room room: hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }



    }

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with Id: "+hotelId));

        List<RoomDto> rooms = hotel.getRooms()
                .stream()
                .map((element)->modelMapper.map(element, RoomDto.class))
                .toList();

        return new HotelInfoDto(modelMapper.map(hotel, HotelDto.class), rooms);
    }
}
