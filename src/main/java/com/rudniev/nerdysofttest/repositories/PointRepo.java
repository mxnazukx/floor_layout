package com.rudniev.nerdysofttest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rudniev.nerdysofttest.entities.Point;

public interface PointRepo extends JpaRepository<Point, Integer>{
	void deleteByRoomId(int roomId);
}
