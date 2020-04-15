package it.cgm.planner.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import it.cgm.planner.model.Group;
import it.cgm.planner.model.UserStudent;
import it.cgm.planner.payload.AddDelGroup;
import it.cgm.planner.payload.ApiResponse;
import it.cgm.planner.payload.GroupRequest;
import it.cgm.planner.repository.GroupRepository;
import it.cgm.planner.repository.UserStudentRepository;

@RestController
@RequestMapping("/groups")
public class GroupController {

	@Autowired
	private GroupRepository groupRepository;
	
	 @Autowired
	 private UserStudentRepository userStudentRepository;
	 
	//role: admin, user
	//get all groups
	@GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN') or harRole('USER')")
	@ResponseBody
	public ResponseEntity<ApiResponse> findAllGroups(HttpServletRequest request) {	

		List<Group> list = new ArrayList<Group>();
		String error = null;
		try {
			list =groupRepository.findAll();
			
		}catch (Exception e) {
			error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, list,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, list, request.getRequestURI()), HttpStatus.OK);
	}
	
	//role: admin, user
	//get all groups
		@GetMapping("/findAllValid") //add , produces = MediaType.A
		@ResponseBody
		public  ResponseEntity<ApiResponse> findAllGroupsValid(HttpServletRequest request) {	
			String error = null;
			List<Group> list = new ArrayList<Group>();
			try {
				list =groupRepository.findByisValidTrueOrderByName();
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
			Optional<Group> group = null;
			try {
				group = groupRepository.findById(id);
			}catch (Exception e) {
				
				error = e.getMessage();
	    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, group,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, group, request.getRequestURI()), HttpStatus.OK);
		}
		
		//role: admin, user
		//insert a group
		@PostMapping("/insert")
		@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
		public ResponseEntity<ApiResponse> saveGroups(@Valid @RequestBody GroupRequest groupRequest,HttpServletRequest request) {
			
			//check if the name of group is already used
			 if(groupRepository.existsByName(groupRequest.getName())) {
		        	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        			HttpStatus.BAD_REQUEST.value(), null, "Name already in use!", request.getRequestURI()), HttpStatus.BAD_REQUEST);
			 }
			    
				Group group = new Group();
				group.setName(groupRequest.getName());
				group.setValid(groupRequest.isValid());
				
			 				
			 groupRepository.save(group);
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        		HttpStatus.OK.value(), null, "Group successfully", request.getRequestURI()), HttpStatus.OK);	
			 }
		
		  
		
		//role: admin, user
		@DeleteMapping("/deleteById/{id}")
		@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
		@ResponseBody
		 public ResponseEntity<ApiResponse> deleteGroupById(@PathVariable Long id, HttpServletRequest request) {
			
			 //check if group selected exist 
			try {
				groupRepository.deleteById(id);
			}catch (Exception e) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "Group don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
	        			}
			return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "Group deleted", request.getRequestURI()), HttpStatus.OK);	
			}
		
		//add user student in a group
		@PutMapping("/update/insertUserGroup")
	    @PreAuthorize("hasRole('ADMIN') or harRole('USER')")
		public ResponseEntity<ApiResponse> replaceGroupAddUser(@Valid @RequestBody AddDelGroup addDelGroup ,HttpServletRequest request) {

			int numberOfInsertions = 0;
		    Set<Long> listOfIdUsersEntered = new HashSet<>();

			int numberOfUserAlreadyPresentInTheGroup = 0;
		    Set<Long> listOfIdUsersAlreadyPresentInTheGroup = new HashSet<>();

			int numberOfUserusersNotFound = 0;
		    Set<Long> listOfIdUsersNotFound = new HashSet<>();

			//list of users to insert that need to be received of List<Long> idUser in addDelGroup 
		    List<Long> idUsers = addDelGroup.getIdUser();
		     
		    //for each id received
		    	 for(Long id : idUsers) {
		    		 
		    		 //get a UserStudent in repository by id
		    	   Optional<UserStudent> user = userStudentRepository.findById(id);
		    	   
		    	   //check: if user is empty the id received is not valid because don't exist a user whit that id
		    	   if(user.isEmpty()) {
		    		    
		    		   numberOfUserusersNotFound ++;		    		  
		    		   listOfIdUsersNotFound.add(id);
		    		   
					}else {
						//get a group whit id received of idGroup in addDelGroup 
			    	   Optional<Group> groupId = groupRepository.findById(addDelGroup.getIdGroup());
			    	   //check: if user is empty the id received is not valid because don't exist a user whit that id
						if(groupId.isEmpty()) {
							return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
				        			HttpStatus.BAD_REQUEST.value(), null, "Group don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
						}
						
						//check if user is present in a group(the UserStudent certainly exist but may not be in the group selected)
						Boolean groupUser = groupRepository.existsByusers(user);
						
						//if gropUser is false the user is not present in the group and map the group by inserting the user
						if(groupUser == false) {
												
							groupId.
							map(group -> {
					    	  group.setId(group.getId());
					    	  group.setName(group.getName());
					    	  group.setValid(group.isValid());
					    	  group.setUsersAdd(user.get());
					    	  
					    	  return groupRepository.save(group);
			 	
						  });
							numberOfInsertions ++;
							listOfIdUsersEntered.add(user.get().getId());
						}else {
							//if groupUser is true the user is already present in a group
							numberOfUserAlreadyPresentInTheGroup ++;
							listOfIdUsersAlreadyPresentInTheGroup.add(id);
						 }
					}
		    	 }
		    	
				return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		 				HttpStatus.OK.value(), null, "numbers of users entered: "+ numberOfInsertions
		 				 +" --|-- "+" list of id Users entered: "+listOfIdUsersEntered
		 				 +" --|-- "+" numbers of users already present in a group: "+numberOfUserAlreadyPresentInTheGroup
		 				+" --|-- "+"list of id Users already present in a group: "+listOfIdUsersAlreadyPresentInTheGroup
		 				+" --|-- "+" numbers of users not found: "+numberOfUserusersNotFound
		 				 +" --|-- "+"list of id Users not found: "+listOfIdUsersNotFound,

		 				 request.getRequestURI()), HttpStatus.OK);			
		}
		
		//delete user student in a group
		@PutMapping("/update/deleteUserGroup")
	    @PreAuthorize("hasRole('ADMIN') or harRole('USER')")
		public ResponseEntity<ApiResponse> replaceGroupDelUser(@Valid @RequestBody AddDelGroup addDelGroup ,HttpServletRequest request) {
			
			int numberOfUserRemoved = 0;
		    Set<Long> listOfIdUserRemoved = new HashSet<>();

		    Set<Long> listOfIdUsersNotPresentInTheGroup = new HashSet<>();

		    int numberOfUserusersNotFound = 0;
		    Set<Long> listOfIdUsersNotFound = new HashSet<>();
		    
			//list of users to delete that need to be received of List<Long> idUser in addDelGroup 
		    List<Long> idUsers = addDelGroup.getIdUser();
		    
		    Optional<Group> groupId = null;
		    
		    //list of all userStudent present in a group
		    Set<UserStudent> listUserStudentInGroup = new HashSet<>();
		    
			//get a group whit id received of idGroup in addDelGroup 
		    groupId = groupRepository.findById(addDelGroup.getIdGroup());
		    
		    	//for each id received
		    	 for(Long id : idUsers) {
		    		 
		    		//get a UserStudent in repository by id
		    	   Optional<UserStudent> user = userStudentRepository.findById(id);
		    	   
		    	   //check: if user is empty the id received is not valid because don't exist a user whit that id
		    	   if(user.isEmpty()) {
		    		    
		    		   numberOfUserusersNotFound ++;		    		  
		    		   listOfIdUsersNotFound.add(id);
		    		   
					}else {
					//check: if user is empty the id received is not valid because don't exist a user whit that id
					if(groupId.isEmpty()) {
						return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
			        			HttpStatus.BAD_REQUEST.value(), null, "Group don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
					}
										
					 listUserStudentInGroup = groupId.get().getUsers();	
					 //for each userStudent inside a list of user student in a group 
					 for (Iterator<UserStudent> it = listUserStudentInGroup.iterator(); it.hasNext();) {
						    UserStudent userStudent = it.next();
						    //if the id user is equal to an id in the list will be deleted 
						    if(user.get().getId() == userStudent.getId()) {
						    	//remove the user student in the group's user student list 
						        it.remove();
						        listOfIdUserRemoved.add(userStudent.getId());
						        numberOfUserRemoved ++;
						    }
						    else {	
						    	//user student exist but is not present in a group
						    	listOfIdUsersNotPresentInTheGroup.add(id);
						    }
						}		
					}		    	  
		    	 }
		    	 groupId.
					map(group -> {			
				    	  
				    	  return groupRepository.save(group);
		 	
					  });
		    	
				return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
						HttpStatus.OK.value(), null, "numbers of users removed: "+ numberOfUserRemoved		
						+" --|-- "+"list of id Users removed: "+listOfIdUserRemoved
		 				+" --|-- "+"list of id Users not present in a group: "+listOfIdUsersNotPresentInTheGroup
		 				+" --|-- "+" numbers of users not found: "+numberOfUserusersNotFound
		 				 +" --|-- "+"list of id Users not found: "+listOfIdUsersNotFound,
		 				 request.getRequestURI()), HttpStatus.OK);			
		}
		
		
		 /* @PostMapping("/users1")
		  @PreAuthorize("hasRole('ADMIN')")
		    public ResponseEntity<ApiResponse> getAllUsers(@Valid @RequestBody GroupRequest groupRequest,HttpServletRequest request ) {
			  
			  if(groupRepository.existsByName(groupRequest.getName())) {
		        	return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        			HttpStatus.BAD_REQUEST.value(), null, "Name already in use!", request.getRequestURI()), HttpStatus.BAD_REQUEST);
			 }
			    
				Group group = new Group();
				group.setName(groupRequest.getName());
				group.setValid(groupRequest.isValid());
				
		    	String error = null;
		    	Optional<UserProfile> userProfile = null;
		    	
		    	try {
		    		userProfile = userRepository.findById(groupRequest.getIdUser())
			        		.map(p -> new UserProfile(p.getId(), p.getUsername(), p.getName(), p.getLastname()));
		    	} catch (Exception e) {
		    		//error = e.getMessage();
		    		//return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, userProfileList, request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        			HttpStatus.BAD_REQUEST.value(), null, "User not set", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		    	}
	        	group.setUsers(Collections.singleton(userProfile));
	        	groupRepository.save(group);
				 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
			        		HttpStatus.OK.value(), null, "Group successfully", request.getRequestURI()), HttpStatus.OK);
		    	//return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, userProfileList, request.getRequestURI()), HttpStatus.OK);
		    	  
		    }*/
}
