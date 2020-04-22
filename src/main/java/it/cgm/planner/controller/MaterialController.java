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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.cgm.planner.model.Argument;
import it.cgm.planner.model.Material;
import it.cgm.planner.payload.ApiResponse;
import it.cgm.planner.payload.MaterialRequest;
import it.cgm.planner.repository.ArgumentRepository;
import it.cgm.planner.repository.MaterialRepository;

@RestController
@RequestMapping("/material")
public class MaterialController {

	@Autowired
	private MaterialRepository materialRepository;
	
	@Autowired
	private ArgumentRepository argumentRepository;
	
	//role: admin, professor
	//get all grades
	@GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN')")
	@ResponseBody
	public ResponseEntity<ApiResponse> findAllGroups(HttpServletRequest request) {	

		List<Material> list = new ArrayList<>();
		String error = null;
		try {
			list =materialRepository.findAll();
			
		}catch (Exception e) {
			error = e.getMessage();
    		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), error, list,  request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), HttpStatus.OK.value(), error, list, request.getRequestURI()), HttpStatus.OK);
	}
	
	//role: admin, user
	//insert a material
	@PostMapping("/insertMaterialInArgument")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> insertGradeInUserSudent(@Valid @RequestBody MaterialRequest materialRequest,HttpServletRequest request) {
		
		Optional<Argument> argument = argumentRepository.findById(materialRequest.getIdArgument());
		//check if userStudent exist
		 if(argument.isEmpty()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "Argument don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
	
		Material material = new Material();
		//set name for a material
		material.setName(materialRequest.getNameMaterial());
		//set content for a material
		material.setContent(materialRequest.getContent());
		
		materialRepository.save(material);
		
		//add a material created in a set of material in to argument
		argument.get().setMaterialAdd(material);
		argumentRepository.save(argument.get());
		
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "Material successfully", request.getRequestURI()), HttpStatus.OK);	
		 }
		
	//role: admin, user
	//delete a one material
	@DeleteMapping("/deleteMaterialInArgument")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> deleteMaterialInArgument(@Valid @RequestBody MaterialRequest materialRequest,HttpServletRequest request) {
		

		Optional<Argument> argument = argumentRepository.findById(materialRequest.getIdArgument());
		//check if userStudent exist
		 if(argument.isEmpty()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "Argument don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		 
		 //for each material in the argument set, if id material in argument it's equal as materialRequest.getIdMaterial, deleted
		 for (Material mr : argument.get().getMaterials()) {
			 if(mr.getId() == materialRequest.getIdMaterial()) {
				 argument.get().setMaterialDel(mr);
				 
					argumentRepository.save(argument.get());
			 }
		 }
		 
		materialRepository.deleteById(materialRequest.getIdMaterial());
		
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "Material deleted", request.getRequestURI()), HttpStatus.OK);	
		 }
		
		//role: admin, user
		//delete all Grade In student
		@DeleteMapping("/deleteAllMaterialInArgument")
	    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
		public ResponseEntity<ApiResponse> deleteAllGradesInStudent(@Valid @RequestBody MaterialRequest materialRequest,HttpServletRequest request) {
					
			Optional<Argument> argument = argumentRepository.findById(materialRequest.getIdArgument());
			//check if userStudent exist
			 if(argument.isEmpty()) {
				 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        			HttpStatus.BAD_REQUEST.value(), null, "Argument don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
			 }
			  //all material in argument selected 
			 Set<Material> materialsInArgument = argument.get().getMaterials();
			//all grades in db
			 List<Material> materials = materialRepository.findAll();
			//list of grades to deleted
			 Set<Material> materialsToDelete = new HashSet<>();
			 //for each material in argument
			 for(Material mat : materialsInArgument) {
				//and for each materials in db
				 for(Material mr : materials) {
					//if the argument material id matches that material id
					 if(mat.getId() == mr.getId()) {
						//add the general grade in the set to deleted 
						 materialsToDelete.add(mr);
					 }
				 }
			 }
			//delete all materials in argument
			argument.get().setMaterialDelCollection();
			
			argumentRepository.save(argument.get());
			//delete all materials in the set
			for(Material m : materialsToDelete) {
				materialRepository.delete(m);
			}		 
			
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        		HttpStatus.OK.value(), null, "material in argument deleted", request.getRequestURI()), HttpStatus.OK);	
		 }
		
		//role: admin, user
		//delete all
		@DeleteMapping("/deleteAll")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<ApiResponse> deleteAll(HttpServletRequest request) {
					
			materialRepository.deleteAll();
			 
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
		        		HttpStatus.OK.value(), null, "material deleted", request.getRequestURI()), HttpStatus.OK);	
		 }
}
