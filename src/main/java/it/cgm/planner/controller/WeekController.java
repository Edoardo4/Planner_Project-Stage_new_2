package it.cgm.planner.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
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
import it.cgm.planner.model.Day;
import it.cgm.planner.model.Group;
import it.cgm.planner.model.Hour;
import it.cgm.planner.model.Room;
import it.cgm.planner.model.UserProfessor;
import it.cgm.planner.model.Week;
import it.cgm.planner.model.WeekName;
import it.cgm.planner.payload.ApiResponse;
import it.cgm.planner.payload.WeekRequest;
import it.cgm.planner.repository.ArgumentRepository;
import it.cgm.planner.repository.DayRepository;
import it.cgm.planner.repository.GroupRepository;
import it.cgm.planner.repository.HourRepository;
import it.cgm.planner.repository.RoomRepository;
import it.cgm.planner.repository.UserProfessorRepository;
import it.cgm.planner.repository.WeekRepository;

@RestController
@RequestMapping("/week")
public class WeekController {

	@Autowired
	private WeekRepository weekRepository;
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private DayRepository dayRepository;
	
	@Autowired
	private HourRepository hourRepository;
	
	@Autowired
	private ArgumentRepository argumentRepository;
	
	@Autowired
	private UserProfessorRepository userProfessorRepository;
	
	@Autowired
	private RoomRepository roomRepository;
	
	//role: admin, user
	//get all week
	@GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	@ResponseBody
	public ResponseEntity<ApiResponse> findAllWeeks(HttpServletRequest request) {	

		List<Week> list = new ArrayList<Week>();
		String error = null;
		try {
			
			list =weekRepository.findAll();
			
		}catch (Exception e) {
			error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, list,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, list, request.getRequestURI()), HttpStatus.OK);
	}
	
	//role: admin, user
	//get week by Id
	@GetMapping("/findById/{id}")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	@ResponseBody
	public ResponseEntity<ApiResponse> findById(@PathVariable Long id, HttpServletRequest request) {	
		Optional<Week> weeks = null;
		String error = null;
		try {
			
			weeks =weekRepository.findById(id);
			
		}catch (Exception e) {
			error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, weeks,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, weeks, request.getRequestURI()), HttpStatus.OK);
	}
	
	//role: admin, user
	//insert week
	@PostMapping("/saveWeek")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> saveWeeks(@Valid @RequestBody WeekRequest weekRequest,HttpServletRequest request) {
		
		//weekBasic is an entity week in which days and hours are already present inside, we will use it to create new weeks starting from that
		Week weekBasic= weekRepository.findByname(WeekName.BASIC_WEEK);
		
		Optional<Group> group = groupRepository.findById(weekRequest.getIdGroup());
		
		//if it already exists we don't create it
		 if(group.isEmpty()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "group don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 
		if(weekBasic != null) {
			
		}else {

			//if it doesn't exist yet I create
			
			//take a list of hour
			List<Hour> hours = hourRepository.findAll();
			//take a list of days
			List<Day> days = dayRepository.findAll();
			
			//for each days in a list of days of weekBasic
			for(Day day : days) {
				//set collection hours whit a list recovered before
				day.setHourAddCollection(hours);
				
				dayRepository.save(day);	
				}
			
			Week week = new Week();
			week.setName(WeekName.BASIC_WEEK);
			//save the list of days set before
			week.setDayAddCollection(days);
			
				weekRepository.save(week);
		}
		
		//take a entity weekBasic save before
		Week week = weekRepository.findByname(WeekName.BASIC_WEEK);
		
		//take a list of hour of weekBasic
		java.util.Set<Day> days = week.getDays();
		//new set<Day> for a new Week
		java.util.Set<Day> daysNew = new HashSet<>() ;
		//new set<Hour> for a new Week
		java.util.Set<Hour> hoursNew = new HashSet<>() ;

		//for each days in a list of days of weekBasic
		for(Day d : days) {
			//for each hours in a day of weekBasic
			for(Hour h : d.getHours()) {
				//create a new entity hour
				Hour hour = new Hour();
				
				hour.setName(h.getName());
				//add our in a new set<Hour> for a new Week
				hoursNew.add(hour);
				
				hourRepository.save(hour);

			}
			//create a new entity Day
			Day day = new Day();
			day.setName(d.getName());
			//save the list of hours set before
			day.setHourAddCollection(hoursNew);
			daysNew.add(day);
			
			dayRepository.save(day);
			
			hoursNew.removeAll(hoursNew);
		}
		//create a new entity week
		Week weekNew = new Week();
		weekNew.setName(weekRequest.getName());
		//save the list of days set before
		weekNew.setDays(daysNew);				
		weekNew.setGroup(group.get().getName());		

		weekRepository.save(weekNew);
				
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "week successfully", request.getRequestURI()), HttpStatus.OK);	
		 }
	
	//role: admin, user
	//complete a week
	@PutMapping("/completeWeek")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> completeWeek(@Valid @RequestBody WeekRequest weekRequest,HttpServletRequest request) {
			
			//select a week to complete
			Optional<Week> weekToComplete= weekRepository.findById(weekRequest.getIdWeek());
			//check if week exist
			if(weekToComplete.isEmpty()) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "Week don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
			}
			//select a argument for the Week to complete
			Optional<Argument> argument = argumentRepository.findById(weekRequest.getIdArgument());
			//check if argument exist
			 if(argument.isEmpty()) {
				 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        			HttpStatus.BAD_REQUEST.value(), null, "Argument don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
			 }
			//select a userProfessor for the Week to complete
			Optional<UserProfessor> userProfessor = userProfessorRepository.findById(weekRequest.getIdUserProfessor());
			//check if userProfessor exist
			 if(userProfessor.isEmpty()) {
				 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        			HttpStatus.BAD_REQUEST.value(), null, "Professor don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
			 }
			//select a room for Week to complete
			 Optional<Room> room= roomRepository.findById(weekRequest.getIdRoom());
			//check if room exist
			 if(room.isEmpty()) {
				 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        			HttpStatus.BAD_REQUEST.value(), null, "Room don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
			 }
					
			 //take a List of Days in week to complete, in this moment the Hour is null
			java.util.Set<Day> daysInweekToComplete = weekToComplete.get().getDays(); 
			
			//for each day in a list of days
			for(Day d : daysInweekToComplete) {
				//check if the day name is equals of day name that we want to set, if they are equals
				if(d.getName() == weekRequest.getDayName()) {
					//take a list of hours in day
					java.util.Set<Hour> hour = d.getHours();
					//for each hour in a list of hours
					for(Hour h : hour) {
						//check if the hour name is equals of hour name that we want to set, if they are equals
						if(h.getName() == weekRequest.getHourName()) {
							h.setArgument(argument.get());
							h.setRoom(room.get());
							h.setUserProfessor(userProfessor.get());
							hourRepository.save(h);
						}
					}
				}
			}
			return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "week successfully", request.getRequestURI()), HttpStatus.OK);	
			}
	
		
		//role: admin, user
		//delete all weeks
		@DeleteMapping("/delete")
	    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
		public ResponseEntity<ApiResponse> delete(HttpServletRequest request) {
					
			 	weekRepository.deleteAll();
			 
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        		HttpStatus.OK.value(), null, "week deleted", request.getRequestURI()), HttpStatus.OK);	
			 }
	
		
		//role: admin, user
		//delete week by id
		@DeleteMapping("/deleteWeekId/{id}")
	    @PreAuthorize("hasRole('ADMIN') or harRole('USER')")
		@ResponseBody
		public ResponseEntity<ApiResponse> deleteWeekById(@PathVariable Long id, HttpServletRequest request) {	
			
			//select a week to delete
			Optional<Week> week = weekRepository.findById(id);
			///list of days in week
			java.util.Set<Day> days = week.get().getDays();
			
			try {
				weekRepository.deleteById(id);
				//for each day in the list, delete
				for(Day d : days) {
					
					dayRepository.deleteById(d.getId());
					//for each hour in the list, delete
					for(Hour h : d.getHours()) {
						
						hourRepository.deleteById(h.getId());
					}
					
				}

			}catch (Exception e) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "week don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "week deleted", request.getRequestURI()), HttpStatus.OK);	
			}
			
	
}
