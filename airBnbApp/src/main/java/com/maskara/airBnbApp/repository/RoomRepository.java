package com.maskara.airBnbApp.repository;

import com.maskara.airBnbApp.modal.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
