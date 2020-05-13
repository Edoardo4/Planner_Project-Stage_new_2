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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.cgm.planner.model.Argument;
import it.cgm.planner.model.Grade;
import it.cgm.planner.model.UserStudent;
import it.cgm.planner.payload.ApiResponse;
import it.cgm.planner.payload.GradeRequest;
import it.cgm.planner.repository.ArgumentRepository;
import it.cgm.planner.repository.GradeRepository;
import it.cgm.planner.repository.UserStudentRepository;

@RestController
@RequestMapping("/grade")
public class GradeController {
	
	@Autowired
	private GradeRepository gradeRepository;
	
	@Autowired
	private ArgumentRepository argumentRepository;
	
	 @Autowired
	 private UserStudentRepository userStudentRepository;
	 
	//role: admin, professor
	//get all grades
	@GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN')")
	@ResponseBody
	public ResponseEntity<ApiResponse> findAllGroups(HttpServletRequest request) {	

		List<Grade> list = new ArrayList<>();
		String error = null;
		try {
			list =gradeRepository.findAll();
			
		}catch (Exception e) {
			error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, list,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, list, request.getRequestURI()), HttpStatus.OK);
	}
	
	//role: admin, user
	//get group by id
	@GetMapping("/byIdArgument/{id}")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> findGroupById(@PathVariable Long id,HttpServletRequest request) {
		String error = null;
		List<Grade> grade = null;
		Optional<Argument> argument = argumentRepository.findById(id);
		try {
			
			grade = gradeRepository.findByargument(argument);
			
		}catch (Exception e) {
			
			error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, grade,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, grade, request.getRequestURI()), HttpStatus.OK);
	}
	
	//role: admin, user
	//insert a grade
	@PostMapping("/insertGradeInUserSudent")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> insertGradeInUserSudent(@Valid @RequestBody GradeRequest gradeRequest,HttpServletRequest request) {
		
		Optional<UserStudent> userStudent = userStudentRepository.findById(gradeRequest.getIdUserStudent());
		//check if userStudent exist
		 if(!userStudent.isPresent()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "userStudent don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 
		Optional<Argument> argument = argumentRepository.findById(gradeRequest.getIdArgument());
		//check if argument exist
		 if(!argument.isPresent()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "argument don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		
		Grade grade = new Grade();
		//set argument for a grade
		grade.setArgument(argument.get());
		//set vote for a grade
		grade.setVote(gradeRequest.getVote());
		
		gradeRepository.save(grade);
		
		//add a grade in a set in the user student
		userStudent.get().setGradesAdd(grade);
		 
		userStudentRepository.save(userStudent.get());
		
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "Grade successfully", request.getRequestURI()), HttpStatus.OK);	
		 }
	
	//role: admin, user
	//delete a one grade
	@DeleteMapping("/deleteGradeInUserSudent")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> deleteGradeInUserSudent(@Valid @RequestBody GradeRequest gradeRequest,HttpServletRequest request) {
		
		Optional<UserStudent> userStudent = userStudentRepository.findById(gradeRequest.getIdUserStudent());
		//check if userStudent exist
		 if(!userStudent.isPresent()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "userStudent don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 //for each grade in the user student set, if id grade in user student it's equal as gradeRequest.getIdGrade, deleted
		 for (Grade gr : userStudent.get().getGrades()) {
			 if(gr.getId() == gradeRequest.getIdGrade()) {
				 userStudent.get().setGradeDel(gr);
				 
				 userStudentRepository.save(userStudent.get());
			 }
		 }
		 
		gradeRepository.deleteById(gradeRequest.getIdGrade());
		
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "Grade deleted", request.getRequestURI()), HttpStatus.OK);	
		 }
	
		//role: admin, user
		//delete all Grade In student
		@DeleteMapping("/deleteAllGradesInStudent")
	    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
		public ResponseEntity<ApiResponse> deleteAllGradesInStudent(@Valid @RequestBody GradeRequest gradeRequest,HttpServletRequest request) {
					
			Optional<UserStudent> userStudent = userStudentRepository.findById(gradeRequest.getIdUserStudent());
			
			//check if userStudent exist
			 if(!userStudent.isPresent()) {
				 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        			HttpStatus.BAD_REQUEST.value(), null, "userStudent don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
			 }
			 //all grade in user student selected 
			 Set<Grade> gradesInUserStudent = userStudent.get().getGrades();
			 //all grades in db
			 List<Grade> grades = gradeRepository.findAll();
			 //list of grades to deleted
			 Set<Grade> gradesToDelete = new HashSet<>();
			 //for each grade in user student
			 for(Grade g : gradesInUserStudent) {
				 //and for each grades in db
				 for(Grade gs : grades) {
					 //if the user student grade id matches that grade id
					 if(g.getId() == gs.getId()) {
						 //add the general grade in the set to deleted 
						 gradesToDelete.add(gs);
					 }
				 }
			 }
			 //delete all grades in user student
			userStudent.get().setGradeDelCollection();
			
			userStudentRepository.save(userStudent.get());
			//delete all grades in the set 
			for(Grade g : gradesToDelete) {
				gradeRepository.delete(g);
			}		 
			
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        		HttpStatus.OK.value(), null, "grade in user Student deleted", request.getRequestURI()), HttpStatus.OK);	
		 }
		
		
		//role: admin, user
		//delete all
		@DeleteMapping("/deleteAll")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<ApiResponse> deleteAll(HttpServletRequest request) {
					
			gradeRepository.deleteAll();
			 
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        		HttpStatus.OK.value(), null, "grade deleted", request.getRequestURI()), HttpStatus.OK);	
		 }
}
