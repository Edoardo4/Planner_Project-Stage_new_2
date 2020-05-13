package it.cgm.planner.controller;

import it.cgm.planner.exception.ResourceNotFoundException;
import it.cgm.planner.model.Role;
import it.cgm.planner.model.RoleName;
import it.cgm.planner.model.User;
import it.cgm.planner.model.UserProfessor;
import it.cgm.planner.model.UserStudent;
import it.cgm.planner.payload.*;
import it.cgm.planner.repository.RoleRepository;
import it.cgm.planner.repository.UserProfessorRepository;
import it.cgm.planner.repository.UserRepository;
import it.cgm.planner.repository.UserStudentRepository;
import it.cgm.planner.security.UserPrincipal;
import it.cgm.planner.security.CurrentUser;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserStudentRepository userStudentRepository;
    
    @Autowired
    private UserProfessorRepository userProfessorRepository;
    
    @Autowired
    private RoleRepository roleRepository;


    //get current student 
    //role_ student, admin 
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getCurrentUser(@CurrentUser UserPrincipal currentUser, HttpServletRequest request) {
    	String error = null;
    	UserSummary userSummary = null;
    	
    	//instantiate the object userSummery whit the attribute the received from currentuser 
    	try {
    	 userSummary = new UserSummary(currentUser.getId(), currentUser.getLastname(), currentUser.getName(), currentUser.getUsername());
    	}catch (Exception e) {
        	error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error,userSummary,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, userSummary, request.getRequestURI()), HttpStatus.OK);
    }

    //check if username is valid 
    @GetMapping("/user/checkUsernameAvailability")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> checkUsernameAvailability(@RequestParam(value = "username") String username, HttpServletRequest request) {
    	String error = null;
    	Boolean isAvailable = null;
    	try {
    	 //is available return true if user is present and false if is not present
    	 isAvailable = !userRepository.existsByUsername(username);
    	}catch (Exception e) {
        	error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error,isAvailable,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, isAvailable, request.getRequestURI()), HttpStatus.OK);
    }
    
    //check if email is valid 
    @GetMapping("/user/checkEmailAvailability")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> checkEmailAvailability(@RequestParam(value = "email") String email, HttpServletRequest request) {
    	String error = null;
    	Boolean isAvailable = null;
    	try {
       	 //is available return true if email is present and false if is not present
    	 isAvailable = !userRepository.existsByEmail(email);
        }catch (Exception e) {
        	error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error,isAvailable,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, isAvailable, request.getRequestURI()), HttpStatus.OK);
    }

    //get user profile whit username
    @GetMapping("/users/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getUserProfile(@PathVariable(value = "username") String username, HttpServletRequest request) {
    	String error = null;
    	 UserProfile userProfile = null;
    	try {
    		//check if user exist
    	 User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    	//instantiate new user profile whit attribute of user
         userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getLastname());
    	}catch (Exception e) {
    		error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error,userProfile,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, userProfile, request.getRequestURI()), HttpStatus.OK);
        
    }

    //get all users
    //role: admin
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers(HttpServletRequest request) {
    	
    	String error = null;
    	//list of all userprofiles
    	List<UserProfile> userProfileList = new ArrayList<UserProfile>();
    
    	try {
    		//map all the students on the list whit UserProfile
    		userProfileList = userRepository.findAll().stream()
	        		.map(p -> new UserProfile(p.getId(), p.getUsername(), p.getName(), p.getLastname()))
	                .collect(Collectors.toList());
    	} catch (Exception e) {
    		error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, userProfileList, request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
        
    	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, userProfileList, request.getRequestURI()), HttpStatus.OK);
    	  
    }
  
    //get user by role
    @GetMapping("/user/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getUserByRole(@RequestParam(value = "roleName") RoleName roleName, HttpServletRequest request){
    	String error = null;
    	
        Optional<Role> role;  
        
        //get a role 
        role = roleRepository.findByName(roleName);
        if(!role.isPresent()) {
        	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
        			HttpStatus.BAD_REQUEST.value(), null, "role don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
        }
        List<UserProfile> userProfileList = new ArrayList<UserProfile>();
        
    	try {
    		//map all the students on the list whit UserProfile
    		userProfileList = userRepository.findByroles(role).stream()
	        		.map(p -> new UserProfile(p.getId(), p.getUsername(), p.getName(), p.getLastname()))
	                .collect(Collectors.toList());

    	}catch (Exception e) {
    		error = e.getMessage();
        	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, userProfileList, request.getRequestURI()), HttpStatus.OK);

		}
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, userProfileList, request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);

    }
    
    //get all user students
    @GetMapping("/usersStudent")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
    public ResponseEntity<ApiResponse> getAllUsersStudent(HttpServletRequest request) {
    	
    	String error = null;
    	List<UserStudent> userStudentList = new ArrayList<UserStudent>();
    	
    	try {
    		userStudentList = userStudentRepository.findAll();
    	} catch (Exception e) {
    		error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, userStudentList, request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
        
    	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, userStudentList, request.getRequestURI()), HttpStatus.OK);
    	  
    }

    //get all user students
    @GetMapping("/usersProfessor")
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

	
	//role: admin, user
	//delete a one material
	@DeleteMapping("/deleteUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteUserProfessor(@PathVariable Long id, HttpServletRequest request) {

		Optional<User> user = userRepository.findById(id);
		
		List<UserProfessor> usersProfessor = userProfessorRepository.findAll();
		List<UserStudent> usersStudent = userStudentRepository.findAll();

		if(!user.isEmpty()) {
			
			for(UserProfessor up: usersProfessor) {
				if(up.getUsername().equals(user.get().getUsername()) ) {
					
					userProfessorRepository.delete(up);
					
				}
			}for(UserStudent us : usersStudent){
					if(us.getUsername().equals(user.get().getUsername())){
				    	String error = "prima eliminare lo studente dalle classe appartenente";
						try {
							userStudentRepository.delete(us);
				    	} catch (Exception e) {
				    		
				    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, us, request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
				    	}
					}	
				}
			
			userRepository.delete(user.get());

			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        		HttpStatus.OK.value(), null, "user deleted", request.getRequestURI()), HttpStatus.OK);	
		}else {
			return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
        			HttpStatus.BAD_REQUEST.value(), null, "user don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		}
		
		 }

}
