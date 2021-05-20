package com.rudniev.nerdysofttest.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.rudniev.nerdysofttest.entities.Point;
import com.rudniev.nerdysofttest.entities.Room;
import com.rudniev.nerdysofttest.others.RoomValidator;
import com.rudniev.nerdysofttest.repositories.PointRepo;
import com.rudniev.nerdysofttest.repositories.RoomRepo;

@Controller
public class LayoutController {

	@Autowired
	PointRepo pointRepo;

	@Autowired
	RoomRepo roomRepo;

	@GetMapping
	public String main(Map<String, Object> model) {
		List<Room> rooms = roomRepo.findAll();
		List<Point> points = pointRepo.findAll();

		model.put("rooms", rooms);
		model.put("points", points);
		return "main";
	}

	@PostMapping
	public String addRoom(@RequestParam String coord, Map<String, Object> model) {
		String message;
		try {
			message = savePoints(coord, 0);
		} catch(Exception e){
			message = "Invalid input";
			e.printStackTrace();
		}
		
		List<Room> rooms = roomRepo.findAll();
		List<Point> points = pointRepo.findAll();
		model.put("message", message);
		model.put("rooms", rooms);
		model.put("points", points);

		return "main";
	}

	public String savePoints(String coord, int id) {
		List<Point> postedPoints = new ArrayList<>();

		
		String[] tokens = coord.substring(1).split(", \\(");

		for (String t : tokens) {
			int x = Integer.parseInt(t.substring(0, t.indexOf(',')).trim());
			int y = Integer.parseInt(t.substring(t.indexOf(',') + 1, t.length() - 1).trim());
			Point point = new Point(x, y, id);
			postedPoints.add(point);
		}

		String message = RoomValidator.validateRoom(postedPoints);
		if (!message.equals(""))
			return message;

		Room room = new Room();
		if(id == 0) {
			roomRepo.save(room);
		} else {
			pointRepo.deleteByRoomId(id);
		}
		for (Point p : postedPoints) {
			if(id == 0) p.setRoom_id(room.getId());
			pointRepo.save(p);
		}
		return "";
	}

	@Transactional
	@GetMapping("/delete/{id}")
	public String deleteRoom(@PathVariable("id") int id, Map<String, Object> model) {

		roomRepo.deleteById(id);
		pointRepo.deleteByRoomId(id);

		List<Room> rooms = roomRepo.findAll();
		List<Point> points = pointRepo.findAll();

		model.put("rooms", rooms);
		model.put("points", points);
		return "redirect:/";

	}

	@Transactional
	@PostMapping("/update/{id}")
	public String updateRoom(@PathVariable("id") int id, @RequestParam String coord, Map<String, Object> model) {
		String message;
		try {
			message = savePoints(coord, id);
		} catch(Exception e) {
			message = "Invalid input";
		}
		model.put("message", message);
		if(!message.equals("")) return "update";

		return "redirect:/";
	}

	@GetMapping("/update/{id}")
	public String redirectToUpdate(@PathVariable("id") int id, Map<String, Object> model) {
		return "update";
	}
	
	@PostMapping("/validateRoom")
	public ResponseEntity<String> validateRoom(@RequestBody String str){
		List<Point> pointList = new ArrayList<>();
		JSONObject o = new JSONObject(str);
		JSONArray arr = o.getJSONArray("room");
		for(int i = 0; i < arr.length(); i++) {
			int x = arr.getJSONObject(i).getInt("x");
			int y = arr.getJSONObject(i).getInt("y");
			pointList.add(new Point(x, y));
		}
		String message = RoomValidator.validateRoom(pointList);
		
		if(!message.equals("")) {
			JSONObject obj = new JSONObject();
			obj.append("error", message);
			return new ResponseEntity<String>(obj.toString(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<String>(str, HttpStatus.OK);
	}
}