package com.rudniev.nerdysofttest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rudniev.nerdysofttest.entities.Room;

public interface RoomRepo extends JpaRepository<Room, Integer>{

}