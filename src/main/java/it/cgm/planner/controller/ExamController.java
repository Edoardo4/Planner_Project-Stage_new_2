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
import it.cgm.planner.model.Exam;
import it.cgm.planner.model.UserStudent;
import it.cgm.planner.payload.ApiResponse;
import it.cgm.planner.payload.ExamRequest;
import it.cgm.planner.repository.ArgumentRepository;
import it.cgm.planner.repository.ExamRepository;
import it.cgm.planner.repository.UserStudentRepository;

@RestController
@RequestMapping("/exam")
public class ExamController {


	@Autowired
	private ExamRepository examRepository;
 
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

		List<Exam> list = new ArrayList<>();
		String error = null;
		try {
			list =examRepository.findAll();
			
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
		List<Exam> exam = null;
		Optional<Argument> argument = argumentRepository.findById(id);
		try {
			
			exam = examRepository.findByargument(argument);
			
		}catch (Exception e) {
			
			error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, exam,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, exam, request.getRequestURI()), HttpStatus.OK);
	}
	
	//role: admin, user
	//insert a grade
	@PostMapping("/insertExamInUserSudent")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> insertExamInUserSudent(@Valid @RequestBody ExamRequest examRequest,HttpServletRequest request) {
		
		Optional<UserStudent> userStudent = userStudentRepository.findById(examRequest.getIdUserStudent());
		//check if userStudent exist
		 if(!userStudent.isPresent()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "userStudent don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 
		Optional<Argument> argument = argumentRepository.findById(examRequest.getIdArgument());
		//check if argument exist
		 if(!argument.isPresent()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "argument don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		
		Exam exam = new Exam();
		
		exam.setArgument(argument.get());
		exam.setName(examRequest.getName());
		exam.setContent(examRequest.getContent());
		exam.setVote(examRequest.getVote());
		
		examRepository.save(exam);
		
		//add a exam in a set in the user student
		userStudent.get().setExamAdd(exam);
		 
		userStudentRepository.save(userStudent.get());
		
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "Exam successfully", request.getRequestURI()), HttpStatus.OK);	
		 }
	
	//role: admin, user
	//delete a one exam
	@DeleteMapping("/deleteExamInUserSudent")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> deleteExamInUserSudent(@Valid @RequestBody ExamRequest examRequest,HttpServletRequest request) {
		
		Optional<UserStudent> userStudent = userStudentRepository.findById(examRequest.getIdUserStudent());
		//check if userStudent exist
		 if(!userStudent.isPresent()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "userStudent don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 //for each exam in the user student set, if id exam in user student it's equal as examRequest.getIExam, deleted
		 for (Exam ex : userStudent.get().getExams()) {
			 if(ex.getId() == examRequest.getIdExam()) {
				 userStudent.get().setExamDel(ex);
				 
				 userStudentRepository.save(userStudent.get());
			 }
		 }
		 
		examRepository.deleteById(examRequest.getIdExam());
		
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "Exam deleted", request.getRequestURI()), HttpStatus.OK);	
		 }
	
	
		//role: admin, user
		//delete all exam In student
		@DeleteMapping("/deleteAllExamsInStudent")
	    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
		public ResponseEntity<ApiResponse> deleteAllExamsInStudent(@Valid @RequestBody ExamRequest examRequest,HttpServletRequest request) {
					
			Optional<UserStudent> userStudent = userStudentRepository.findById(examRequest.getIdUserStudent());
			
			//check if userStudent exist
			 if(!userStudent.isPresent()) {
				 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        			HttpStatus.BAD_REQUEST.value(), null, "userStudent don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
			 }
			 //all exam in user student selected 
			 Set<Exam> examInUserStudent = userStudent.get().getExams();
			 //all exam in db
			 List<Exam> exams = examRepository.findAll();
			 //list of exams to deleted
			 Set<Exam> examsToDelete = new HashSet<>();
			 //for each exam in user student
			 for(Exam ex : examInUserStudent) {
				 //and for each exams in db
				 for(Exam e : exams) {
					 //if the user student exam id matches that exam id
					 if(ex.getId() == e.getId()) {
						 //add the general exam in the set to deleted 
						 examsToDelete.add(e);
					 }
				 }
			 }
			 //delete all exams in user student
			userStudent.get().setExamDelCollection();
			
			userStudentRepository.save(userStudent.get());
			//delete all exams in the set 
			for(Exam ex : examsToDelete) {
				examRepository.delete(ex);
			}		 
			
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        		HttpStatus.OK.value(), null, "grade in user Student deleted", request.getRequestURI()), HttpStatus.OK);	
		 }
		
		
		//role: admin, user
		//delete all
		@DeleteMapping("/deleteAll")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<ApiResponse> deleteAll(HttpServletRequest request) {
					
			examRepository.deleteAll();
			 
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        		HttpStatus.OK.value(), null, "exam deleted", request.getRequestURI()), HttpStatus.OK);	
		 }
}
