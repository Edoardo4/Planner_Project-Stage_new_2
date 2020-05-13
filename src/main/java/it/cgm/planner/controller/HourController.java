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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.cgm.planner.model.Argument;
import it.cgm.planner.model.Hour;
import it.cgm.planner.model.Room;
import it.cgm.planner.model.UserProfessor;
import it.cgm.planner.payload.ApiResponse;
import it.cgm.planner.payload.HourRequest;
import it.cgm.planner.repository.ArgumentRepository;
import it.cgm.planner.repository.HourRepository;
import it.cgm.planner.repository.RoomRepository;
import it.cgm.planner.repository.UserProfessorRepository;

@RestController
@RequestMapping("/hours")
public class HourController {
	
	@Autowired
	private HourRepository hourRepository;
	
	@Autowired
	private ArgumentRepository argumentRepository;
	
	@Autowired
	private UserProfessorRepository userProfessorRepository;
	
	@Autowired
	private RoomRepository roomRepository;
	
	//role: admin, user
	//get all hours
	@GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN')")
	@ResponseBody
	public ResponseEntity<ApiResponse> findAllHours(HttpServletRequest request) {	

		List<Hour> list = new ArrayList<Hour>();
		String error = null;
		try {
			
			list =hourRepository.findAll();
			
		}catch (Exception e) {
			error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, list,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, list, request.getRequestURI()), HttpStatus.OK);
	}
	
		//role: admin, user
		//get hour by id
		@GetMapping("/byId/{id}")
	    @PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<ApiResponse> findHourById(@PathVariable Long id,HttpServletRequest request) {
			
			String error = null;
			Optional<Hour> hour = null;
			try {
				
				hour = hourRepository.findById(id);
				
			}catch (Exception e) {
				
				error = e.getMessage();
	    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, hour,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, hour, request.getRequestURI()), HttpStatus.OK);
		}

		//role: admin, user
		//insert a group
		@PostMapping("/insert")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<ApiResponse> saveDay(@Valid @RequestBody HourRequest hourRequest, HttpServletRequest request) {
  
				Hour hour = new Hour();
				hour.setName(hourRequest.getName());
			 				
				hourRepository.save(hour);
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        		HttpStatus.OK.value(), null, "hour successfully", request.getRequestURI()), HttpStatus.OK);	
			 }
		
		//role: admin, user
		//insert a hour
		@PutMapping("/saveHour")
	    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
		public ResponseEntity<ApiResponse> saveHour(@Valid @RequestBody HourRequest hourRequest,HttpServletRequest request) {
			
		Optional<Hour> hourId = hourRepository.findById(hourRequest.getIdHour());
		
		//check if hour exist
		 if(!hourId.isPresent()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "Hour don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 
		Optional<Argument> argument = argumentRepository.findById(hourRequest.getIdArgument());

		 if(!argument.isPresent()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "Argument don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 
		Optional<UserProfessor> userProfessor = userProfessorRepository.findById(hourRequest.getIdUserProfessor());

		 if(!userProfessor.isPresent()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "Professor don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 
		 Optional<Room> room= roomRepository.findById(hourRequest.getIdRoom());

		 if(!room.isPresent()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "room don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 hourId.
			map(hour -> {
				hour.setId(hour.getId());
				hour.setName(hour.getName());
				hour.setRoom(room.get());
				hour.setUserProfessor(userProfessor.get());
				hour.setArgument(argument.get());
				
	    	  return hourRepository.save(hour);

		  });
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "Hour changed", request.getRequestURI()), HttpStatus.OK);	
			 }
		
		//role: admin, user
		//delete all
		@DeleteMapping("/delete")
	    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
		public ResponseEntity<ApiResponse> delete(HttpServletRequest request) {
					
			 	hourRepository.deleteAll();
			 
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        		HttpStatus.OK.value(), null, "hour deleted", request.getRequestURI()), HttpStatus.OK);	
			 }
	
}
