package com.maskara.airBnbApp.repository;

import com.maskara.airBnbApp.modal.Hotel;
import com.maskara.airBnbApp.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> finByOwner(User user);
}
