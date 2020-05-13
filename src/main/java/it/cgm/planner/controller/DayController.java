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

import it.cgm.planner.model.Day;
import it.cgm.planner.model.Hour;
import it.cgm.planner.payload.ApiResponse;
import it.cgm.planner.payload.DayRequest;
import it.cgm.planner.repository.DayRepository;
import it.cgm.planner.repository.HourRepository;

@RestController
@RequestMapping("/day")
public class DayController {

	@Autowired
	private DayRepository dayRepository;
	
	@Autowired
	private HourRepository hourRepository;
	//role: admin, user
	//get all day
	@GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN')")
	@ResponseBody
	public ResponseEntity<ApiResponse> findAllDays(HttpServletRequest request) {	

		List<Day> list = new ArrayList<Day>();
		String error = null;
		try {
			
			list =dayRepository.findAll();
			
		}catch (Exception e) {
			error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, list,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, list, request.getRequestURI()), HttpStatus.OK);
	}
	
	//role: admin, user
	//get day by id
	@GetMapping("/byId/{id}")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> findDayById(@PathVariable Long id,HttpServletRequest request) {
		
		String error = null;
		Optional<Day> day = null;
		try {
			
			day = dayRepository.findById(id);
			
		}catch (Exception e) {
			
			error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, day,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, day, request.getRequestURI()), HttpStatus.OK);
	}
	
	//role: admin, user
	//insert a group
	@PostMapping("/insert")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> saveDay(@Valid @RequestBody DayRequest dayRequest,HttpServletRequest request) {
		
		//check if the name of group is already used
		 if(dayRepository.existsByname(dayRequest.getName())) {
	        	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "Name already in use!", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		    
			Day day = new Day();
			day.setName(dayRequest.getName());
		 				
			dayRepository.save(day);
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "day successfully", request.getRequestURI()), HttpStatus.OK);	
		 }
	
		//role: admin, user
		//insert a hour
		@PutMapping("/saveHourInDay")
		@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
		public ResponseEntity<ApiResponse> saveHourInDay(@Valid @RequestBody DayRequest dayRequest,HttpServletRequest request) {
			
		Optional<Day> days = dayRepository.findById(dayRequest.getIdDay());
		
		List<Long> hours = dayRequest.getIdHours();
		
		//check if day exist
		 if(!days.isPresent()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "Days don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		for(Long id: hours) {
			
			Optional<Hour> hour = hourRepository.findById(id);
			
			if(!hour.isPresent()) {				
				days.get().setHourAdd(hour.get());
			}
		}
		days.
		map(day -> {
			
    	  return dayRepository.save(day);
		});		
		
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "Day changed", request.getRequestURI()), HttpStatus.OK);	
			 }
		
		//role: admin, user
		//delete a hour
		@PutMapping("/deleteHourInDay")
	    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
		public ResponseEntity<ApiResponse> deleteHourInDay(@Valid @RequestBody DayRequest dayRequest,HttpServletRequest request) {
			
		Optional<Day> days = dayRepository.findById(dayRequest.getIdDay());
		
		//check if day exist
		 if(!days.isPresent()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "Days don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 days.get().setUsersDelCollection();
		 
		days.
		map(day -> {
			
    	  return dayRepository.save(day);
		});		
		
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "Hours deleted", request.getRequestURI()), HttpStatus.OK);	
			 }
		
		//role: admin, user
		//delete day
		@DeleteMapping("/deleteDay/{id}")
	    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
		@ResponseBody
		public ResponseEntity<ApiResponse> deleteDay(@PathVariable Long id, HttpServletRequest request) {	
			try {
				
				dayRepository.deleteById(id);
				
			}catch (Exception e) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "day don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "day deleted", request.getRequestURI()), HttpStatus.OK);	
			}
		
		//role: admin, user
		//delete all
		@DeleteMapping("/delete")
	    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
		public ResponseEntity<ApiResponse> delete(HttpServletRequest request) {
					
			 	dayRepository.deleteAll();
			 
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        		HttpStatus.OK.value(), null, "day deleted", request.getRequestURI()), HttpStatus.OK);	
			 }
}
