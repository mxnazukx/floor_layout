package com.rudniev.nerdysofttest.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity(name="points")
public class Point {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;	
	@Column(name="x")
	private int x;
	@Column(name="y")
	private int y;
	@Column(name="room_id")
	private int roomId;
	
	public Point() {
	}
	public Point(int x, int y, int roomId) {
		this.x = x;
		this.y = y;
		this.roomId = roomId;
	}
	
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoom_id(int roomId) {
		this.roomId = roomId;
	}
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + ", roomId=" + roomId + "]";
	}
}
