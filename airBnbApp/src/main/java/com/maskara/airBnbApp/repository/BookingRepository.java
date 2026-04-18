package com.maskara.airBnbApp.repository;

import com.maskara.airBnbApp.modal.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
