package it.cgm.planner.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.cgm.planner.model.Room;
import it.cgm.planner.payload.ApiResponse;
import it.cgm.planner.repository.RoomRepository;

@RestController
@RequestMapping("/room")
public class RoomController {
	
	@Autowired
	private RoomRepository roomRepository;
	
	//role: admin, user
	//get all groups
	@GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN') or harRole('USER')")
	@ResponseBody
	public ResponseEntity<ApiResponse> findAllGroups(HttpServletRequest request) {	

		List<Room> list = new ArrayList<Room>();
		String error = null;
		try {
			list =roomRepository.findAll();
			
		}catch (Exception e) {
			error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, list,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, list, request.getRequestURI()), HttpStatus.OK);
	}
	

	//role: admin, user
	//get group by id
	@GetMapping("/byId/{id}")
    @PreAuthorize("hasRole('ADMIN') or harRole('USER')")
	public ResponseEntity<ApiResponse> findGroupById(@PathVariable Long id,HttpServletRequest request) {
		String error = null;
		Optional<Room> room = null;
		try {
			room = roomRepository.findById(id);
		}catch (Exception e) {
			
			error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, room,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, room, request.getRequestURI()), HttpStatus.OK);
	}
	
	//role: admin, user
	//insert a room
	@PostMapping("/insert")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> saveGroups(@Valid @RequestBody Room room,HttpServletRequest request) {
		
		//check if the name of group is already used
		 if(roomRepository.existsByName(room.getName())) {
	        	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "Name already in use!", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		    
			Room newRoom = new Room();
			newRoom.setName(room.getName());
			newRoom.setValid(room.isValid());
			
		 roomRepository.save(newRoom);
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "room successfully", request.getRequestURI()), HttpStatus.OK);	
		 }
	
	
	//role: admin, user
	@DeleteMapping("/deleteById/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@ResponseBody
	 public ResponseEntity<ApiResponse> deleteGroupById(@PathVariable Long id, HttpServletRequest request) {
		
		 //check if group selected exist 
		try {
			roomRepository.deleteById(id);
		}catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
        			HttpStatus.BAD_REQUEST.value(), null, "room don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
        			}
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
        		HttpStatus.OK.value(), null, "room deleted", request.getRequestURI()), HttpStatus.OK);	
		}
	
}
