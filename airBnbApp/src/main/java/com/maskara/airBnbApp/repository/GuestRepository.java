package com.maskara.airBnbApp.repository;

import com.maskara.airBnbApp.modal.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long > {
}
