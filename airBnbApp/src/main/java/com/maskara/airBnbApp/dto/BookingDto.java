package com.maskara.airBnbApp.dto;

import com.maskara.airBnbApp.modal.Guest;
import com.maskara.airBnbApp.modal.Hotel;
import com.maskara.airBnbApp.modal.Room;
import com.maskara.airBnbApp.modal.enums.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDto {
    private Long id;

    private Integer roomsCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BookingStatus bookingStatus;
    private Set<GuestDto> guests;
    private BigDecimal amount;
}
