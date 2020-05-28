package it.cgm.planner.controller;

import it.cgm.planner.model.Role;
import it.cgm.planner.model.User;
import it.cgm.planner.model.UserProfessor;
import it.cgm.planner.model.UserStudent;
import it.cgm.planner.payload.ApiResponse;
import it.cgm.planner.payload.JwtAuthenticationResponse;
import it.cgm.planner.payload.LoginRequest;
import it.cgm.planner.payload.SignUpRequest;
import it.cgm.planner.repository.RoleRepository;
import it.cgm.planner.repository.UserProfessorRepository;
import it.cgm.planner.repository.UserRepository;
import it.cgm.planner.repository.UserStudentRepository;
import it.cgm.planner.security.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.File;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProfessorRepository userProfessorRepository;

    @Autowired
    UserStudentRepository userStudentRepository;
    
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public String authenticateUser(@Valid @RequestParam(value = "username") String username, @RequestParam(value = "password") String password
    		/*@RequestBody LoginRequest loginRequest*/) {

    	Optional<User> u = userRepository.findByEmail(/*loginRequest.getUsername()*/username);
    	
    	if(!u.isPresent()) {
      	   
             return "error/403";
         }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        //loginRequest.getUsername(),
                		u.get().getUsername(),
                		password                       /*loginRequest.getPassword()*/

                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        
        Set<Role> role= u.get().getRoles();
       for(Role r: role) {
    	   
    	   if(r.getName().name() == "ROLE_ADMIN") {
    		   return "admin";
    	   }
    	   else if(r.getName().name() == "ROLE_STUDENT") {
    		   
               return "student";

    	   }
    	   else if(r.getName().name() == "ROLE_PROFESSOR") {
	    		   
	               return "professor";
       }
       }
	return jwt;
      
        //return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    /*
     *  
     * */
	@PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest, HttpServletRequest request) {
    	
		// Setting username
    	String username = signUpRequest.getName().concat(".").concat(signUpRequest.getLastname());
    	
    	// In case of homonymy a counter will be added to username
    	long count = userRepository.countByNameAndLastname(signUpRequest.getName(), signUpRequest.getLastname());
    	if(count > 0) {
    		username = username+count;
    	}
    	
    	// Ctrl if user email exists
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
        	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
        			HttpStatus.BAD_REQUEST.value(), null, "Email Address already in use!", request.getRequestURI()), HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getLastname(), 
        		username, signUpRequest.getEmail(), signUpRequest.getPassword());
        
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        //Set Role to new user
        Optional<Role> userRole = roleRepository.findByName(signUpRequest.getRoleName());
        if(userRole.isPresent()) {
        	user.setRoles(Collections.singleton(userRole.get()));
        } else {
        	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
        			HttpStatus.BAD_REQUEST.value(), null, "User Role not set", request.getRequestURI()), HttpStatus.BAD_REQUEST);
        }
        // Save user
        userRepository.saveAndFlush(user);

        //if role name = a role_student insert an another entity the user
        if(signUpRequest.getRoleName().name() == "ROLE_STUDENT") {
        	UserStudent userStudent = new UserStudent(signUpRequest.getName(), signUpRequest.getLastname(), username);
        	//save the user in UserStudent entity
			userStudentRepository.saveAndFlush(userStudent);
			  return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        		HttpStatus.OK.value(), null, "User registered successfully", request.getRequestURI()), HttpStatus.OK);
        }
        
        if(signUpRequest.getRoleName().name() == "ROLE_PROFESSOR") {
        	UserProfessor userProfessor = new UserProfessor(signUpRequest.getName(), signUpRequest.getLastname(), username);
			//save the user in UserStudent entity
			userProfessorRepository.saveAndFlush(userProfessor);
			
			 File file = new File("/home/edoardo/Documenti/professorMaterial/"+username);
			  
		        if (!file.exists()) {
		        	
		            file.mkdirs();            
		        }
		        
			  return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        		HttpStatus.OK.value(), null, "User registered successfully", request.getRequestURI()), HttpStatus.OK);	  
			 
        }
        
        return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
        		HttpStatus.OK.value(), null, "User registered successfully", request.getRequestURI()), HttpStatus.OK);
    }
    
	// Cookies Management for 'Remember me' flag
	//@ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/writeCookie")
    public ResponseEntity writeCookie() {

        String favColour = "steelblue";
        Object cookie = ResponseCookie.from("fav-col", favColour).build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
	
	//@ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/readCookie")
    public void readCookie(@CookieValue(value = "fav-col",
            defaultValue = "unknown") String favColour) {

        System.out.println("Favourite colour: "+ favColour);
    }
}
