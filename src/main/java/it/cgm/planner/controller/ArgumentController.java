package it.cgm.planner.controller;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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
import it.cgm.planner.payload.ApiResponse;
import it.cgm.planner.repository.ArgumentRepository;

@RestController
@RequestMapping("/arguments")
public class ArgumentController {
	
	@Autowired
	private ArgumentRepository argumentRepository;
	
	// role: admin, user
	//get all argument  
	@GetMapping("/findAll") 
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	@ResponseBody
	public ResponseEntity<ApiResponse> findAllArguments(HttpServletRequest request) {	

		List<Argument> list = new ArrayList<Argument>();
		String error = null;
		try {
			list =argumentRepository.findAll();
			
		}catch (Exception e) {
			error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, list,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, list, request.getRequestURI()), HttpStatus.OK);
	}
	
	//role: admin, user
	//get all argument valid
	@GetMapping("/findAllValid") 
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	@ResponseBody
	public  ResponseEntity<ApiResponse> findAllArgumentsValid(HttpServletRequest request) {	
		String error = null;
		List<Argument> list = new ArrayList<Argument>();
		try {
			list =argumentRepository.findByisValidTrueOrderByName();
		}catch (Exception e) {
			error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, list,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);		
    		}
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, list, request.getRequestURI()), HttpStatus.OK);
	}
	
	//role: admin, user
	//get argument by id
	@GetMapping("/byId/{id}")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> findById(@PathVariable Long id,HttpServletRequest request) {
		String error = null;
		Optional<Argument> argument = null;
		try {
			argument = argumentRepository.findById(id);
		}catch (Exception e) {
			
			error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, argument,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, argument, request.getRequestURI()), HttpStatus.OK);
	}
	
	//role: admin, user
	//insert a new argument
	@PostMapping("/insert")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> saveArgument(@RequestBody Argument newArgument,HttpServletRequest request ) {
		
		//check if the name of argument is already used
		 if(argumentRepository.existsByName(newArgument.getName())) {
	        	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "Name already used!", request.getRequestURI()), HttpStatus.BAD_REQUEST);
	        }
		 
		 //check if the code of argument is already used
		 if(argumentRepository.existsByCode(newArgument.getCode())) {
	        	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "Code already used!", request.getRequestURI()), HttpStatus.BAD_REQUEST);
	        }
		 
		Argument argument = new Argument();
		argument.setCode(newArgument.getCode());
		argument.setName(newArgument.getName());
		argument.setValid(newArgument.isValid());
				
		argumentRepository.save(argument);
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "Argument successfully", request.getRequestURI()), HttpStatus.OK);	
		 }
	
	//role: admin, user
	//delete a argument
	@DeleteMapping("/deleteById/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseBody
	 public ResponseEntity<ApiResponse> deleteArgumentById(@PathVariable Long id, HttpServletRequest request) {
		try {
			 argumentRepository.deleteById(id);
			 
			 //check if argument selected exist 
		}catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
        			HttpStatus.BAD_REQUEST.value(), null, "Argument don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
        			}
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
        		HttpStatus.OK.value(), null, "Argument deleted", request.getRequestURI()), HttpStatus.OK);	
		}
	
	//role: admin, user
	//update a argument
	@PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
	public void replaceArgument(@RequestBody Argument newArgument, @PathVariable Long id) {
	     argumentRepository.findById(id)
	      .map(argument -> {
	    	  argument.setName(newArgument.getName());
	    	  argument.setCode(newArgument.getCode());
	    	  argument.setValid(newArgument.isValid());

	        return argumentRepository.save(newArgument);
	      })
	      .orElseGet(() -> {
	    	  newArgument.setId(id);
	        return argumentRepository.save(newArgument);
	      });
	  }
}