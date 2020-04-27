package it.cgm.planner.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RestController;

import it.cgm.planner.model.Argument;
import it.cgm.planner.model.Exam;
import it.cgm.planner.model.Group;

import it.cgm.planner.model.UserProfessor;
import it.cgm.planner.payload.ApiResponse;

import it.cgm.planner.payload.UserProfessorRequest;
import it.cgm.planner.repository.ArgumentRepository;
import it.cgm.planner.repository.ExamRepository;
import it.cgm.planner.repository.GroupRepository;
import it.cgm.planner.repository.UserProfessorRepository;

@RestController
@RequestMapping("/userProfessor")
public class UserProfessorController {

	@Autowired
    private UserProfessorRepository userProfessorRepository;
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private ArgumentRepository argumentRepository;
	
	@Autowired
	private ExamRepository examRepository;
	
	//get all user students
    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
    public ResponseEntity<ApiResponse> getAllUsersProfessor(HttpServletRequest request) {
    	
    	String error = null;
    	List<UserProfessor> userProfessorList = new ArrayList<UserProfessor>();
    	
    	try {
    		userProfessorList = userProfessorRepository.findAll();
    	} catch (Exception e) {
    		error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, userProfessorList, request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
        
    	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, userProfessorList, request.getRequestURI()), HttpStatus.OK);
    }
    
    //get user whit username
    @GetMapping("/byUserName/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getUser(@PathVariable(value = "username") String username, HttpServletRequest request) {
    	String error = null;
    	Optional<UserProfessor> user = null;
    	try {
    		//check if user exist
    		 user = userProfessorRepository.findByUsername(username);
         
    	}catch (Exception e) {
    		error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, user,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, user, request.getRequestURI()), HttpStatus.OK);
    }
    
    //get all group
    @GetMapping("/allGroup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getGroup(@Valid @RequestBody UserProfessorRequest userProfessorRequest, HttpServletRequest request) {
    	String error = null;
    	
    	Optional<UserProfessor> userProfessor;
    	
         Set<Group> groups = new HashSet<>();
    	
    	try {
    		//check if user exist
    		userProfessor = userProfessorRepository.findByUsername(userProfessorRequest.getUsername());
    		
    		groups = userProfessor.get().getGroups();
         
    	}catch (Exception e) {
    		error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, groups,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, groups, request.getRequestURI()), HttpStatus.OK);
    }
    
    //get all argument
    @GetMapping("/allArgument")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getArgument(@Valid @RequestBody UserProfessorRequest userProfessorRequest, HttpServletRequest request) {
    	String error = null;
    	
    	Optional<UserProfessor> userProfessor;
    	
         Set<Argument> argument = new HashSet<>();
    	
    	try {
    		//check if user exist
    		userProfessor = userProfessorRepository.findByUsername(userProfessorRequest.getUsername());
    		
    		argument = userProfessor.get().getArguments();
         
    	}catch (Exception e) {
    		error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, argument,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, argument, request.getRequestURI()), HttpStatus.OK);
    }
    
    //get all exam
    @GetMapping("/allExam")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getExam(@Valid @RequestBody UserProfessorRequest userProfessorRequest, HttpServletRequest request) {
    	String error = null;
    	
    	Optional<UserProfessor> userProfessor;
    	
         Set<Exam> exam = new HashSet<>();
    	
    	try {
    		//check if user exist
    		userProfessor = userProfessorRepository.findByUsername(userProfessorRequest.getUsername());
    		
    		exam = userProfessor.get().getExams();
         
    	}catch (Exception e) {
    		error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, exam,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, exam, request.getRequestURI()), HttpStatus.OK);
    }
    
    
    
    
	//role: admin, user
	//insert a grade
	@PostMapping("/insertGroupInUserProfessor")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> insertGroupInUserProfessor(@Valid @RequestBody UserProfessorRequest userProfessorRequest,HttpServletRequest request) {
		
		Optional<UserProfessor> userProfessor = userProfessorRepository.findByUsername(userProfessorRequest.getUsername());
		//check if userStudent exist
		 if(userProfessor.isEmpty()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "userStudent don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 
		Optional<Group> group = groupRepository.findById(userProfessorRequest.getIdGroup());
		//check if argument exist
		 if(group.isEmpty()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "group don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 
			//add a group in a set in the user professor		 
		 userProfessor.get().setGroupAdd(group.get());
		 
		 userProfessorRepository.save(userProfessor.get());
		
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "group successfully", request.getRequestURI()), HttpStatus.OK);	
		 }
	
	//role: admin, user
	//delete a one material
	@DeleteMapping("/deleteGroupInUserProfessor")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> deleteGroupInUserProfessor(@Valid @RequestBody UserProfessorRequest userProfessorRequest, HttpServletRequest request) {
		

		Optional<UserProfessor> userProfessor = userProfessorRepository.findByUsername(userProfessorRequest.getUsername());
		//check if userStudent exist
		 if(userProfessor.isEmpty()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "userStudent don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 
		 //for each group in the group set, if id group in user professor it's equal as userProfessorRequest..getIdGroup(), deleted
		 for (Group gr : userProfessor.get().getGroups()) {
			 if(gr.getId() == userProfessorRequest.getIdGroup()) {
				 userProfessor.get().setGroupDel(gr);
				 
				 userProfessorRepository.save(userProfessor.get());
			 }
		 }
		 
		 
		 		
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "Group in User Professor deleted", request.getRequestURI()), HttpStatus.OK);	
		 }
    
	
	
	
	
	//role: admin, user
	//insert a grade
	@PostMapping("/insertArgumentInUserProfessor")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> insertArgumentInUserProfessor(@Valid @RequestBody UserProfessorRequest userProfessorRequest,HttpServletRequest request) {
		
		Optional<UserProfessor> userProfessor = userProfessorRepository.findByUsername(userProfessorRequest.getUsername());
		//check if userStudent exist
		 if(userProfessor.isEmpty()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "userStudent don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 
		Optional<Argument> argument = argumentRepository.findById(userProfessorRequest.getIdArgument());
		//check if argument exist
		 if(argument.isEmpty()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "argument don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		
		 userProfessor.get().setArgumentAdd(argument.get());
		//add a argument in a set in the user professor		 
		 userProfessorRepository.save(userProfessor.get());
		
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "argument successfully", request.getRequestURI()), HttpStatus.OK);	
		 }
	
	//role: admin, user
	//delete a one material
	@DeleteMapping("/deleteArgumentInUserProfessor")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> deleteArgumentInUserProfessor(@Valid @RequestBody UserProfessorRequest userProfessorRequest, HttpServletRequest request) {
		

		Optional<UserProfessor> userProfessor = userProfessorRepository.findByUsername(userProfessorRequest.getUsername());
		//check if userStudent exist
		 if(userProfessor.isEmpty()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "userStudent don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 
		 for (Argument ar : userProfessor.get().getArguments()) {
			 if(ar.getId() == userProfessorRequest.getIdArgument()) {
				 userProfessor.get().setArgumentDel(ar);
				 
				 userProfessorRepository.save(userProfessor.get());
			 }
		 }
		 		
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "argument in User Professor deleted", request.getRequestURI()), HttpStatus.OK);	
		 }
	
	
	
	
	
	
	//role: admin, user
	//insert a grade
	@PostMapping("/insertExamInUserProfessor")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> insertExamInUserProfessor(@Valid @RequestBody UserProfessorRequest userProfessorRequest,HttpServletRequest request) {
		
		Optional<UserProfessor> userProfessor = userProfessorRepository.findByUsername(userProfessorRequest.getUsername());
		//check if userStudent exist
		 if(userProfessor.isEmpty()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "userStudent don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		
		Optional<Argument> argument = argumentRepository.findById(userProfessorRequest.getIdArgument());
		//check if argument exist
		 if(argument.isEmpty()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "argument don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
			 
		 Exam exam = new Exam();
		 exam.setArgument(argument.get());
		 exam.setContent(userProfessorRequest.getContent());
		 exam.setName(userProfessorRequest.getName());
		 exam.setVote(userProfessorRequest.getVote());
		
		 examRepository.save(exam);
		 
		 userProfessor.get().setExamAdd(exam);
		//add a exam in a set in the user student		 
		 userProfessorRepository.save(userProfessor.get());
		
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "exam successfully", request.getRequestURI()), HttpStatus.OK);	
		 }
	
	//role: admin, user
	//delete a one material
	@DeleteMapping("/deleteExamInUserProfessor")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> deleteExamInUserProfessor(@Valid @RequestBody UserProfessorRequest userProfessorRequest, HttpServletRequest request) {
		

		Optional<UserProfessor> userProfessor = userProfessorRepository.findByUsername(userProfessorRequest.getUsername());
		//check if userStudent exist
		 if(userProfessor.isEmpty()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "userStudent don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 
		 for (Exam ex : userProfessor.get().getExams()) {
			 if(ex.getId() == userProfessorRequest.getIdExam()) {
				 userProfessor.get().setExamDel(ex);
				 
				 userProfessorRepository.save(userProfessor.get());
				 
				 examRepository.deleteById(ex.getId());
				 
			 }
		 }
		 		
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "exam in User Professor deleted", request.getRequestURI()), HttpStatus.OK);	
		 }
	
	
}
