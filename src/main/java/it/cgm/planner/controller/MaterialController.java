package it.cgm.planner.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import it.cgm.planner.model.Argument;
import it.cgm.planner.model.Material;
import it.cgm.planner.model.UserProfessor;
import it.cgm.planner.payload.ApiResponse;
import it.cgm.planner.payload.MaterialRequest;
import it.cgm.planner.repository.ArgumentRepository;
import it.cgm.planner.repository.MaterialRepository;
import it.cgm.planner.repository.UserProfessorRepository;
import it.cgm.planner.security.CurrentUser;
import it.cgm.planner.security.MediaTypeUtils;
import it.cgm.planner.security.UserPrincipal;

@RestController
@RequestMapping("/material")
public class MaterialController {

	@Autowired
	private MaterialRepository materialRepository;
	
	@Autowired
	private ArgumentRepository argumentRepository;
	
	@Autowired
	private UserProfessorRepository userProfessorRepository;
	
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> insertGradeInUserSudent(@Valid @RequestBody MaterialRequest materialRequest,HttpServletRequest request) {
		
		Optional<Argument> argument = argumentRepository.findById(materialRequest.getIdArgument());
		//check if userStudent exist
		 if(!argument.isPresent()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "Argument don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
	
		Material material = new Material();
		
		//set name for a material
		material.setRealName(materialRequest.getRealName());
		
		//il file viene caricato nel mio pc, nel db viene inserito una stringa con il path del file 
		//path del file
		material.setServerFile(materialRequest.getServerFile());
		
		materialRepository.save(material);
		
		//add a material created in a set of material in to argument
		argument.get().setMaterialAdd(material);
		argumentRepository.save(argument.get());
		
		 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        		HttpStatus.OK.value(), null, "Material successfully", request.getRequestURI()), HttpStatus.OK);	
		 }
   
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<ApiResponse> uploadFile(@RequestParam(value = "file") MultipartFile uploadfile,@RequestParam(value = "idArgument") Long id, HttpServletRequest request,@CurrentUser UserPrincipal currentUser) throws MaxUploadSizeExceededException {
        //Save the uploaded file to this folder
      String UPLOADED_FOLDER = "/home/edoardo/Documenti/professorMaterial/";
        // 3.1.1 Single file upload
    	    	
    	Optional<Argument> argument = argumentRepository.findById(id);
		//check if userStudent exist
		 if(!argument.isPresent()) {
			 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "Argument don't exist", request.getRequestURI()), HttpStatus.BAD_REQUEST);
		 }
		
		if (uploadfile.isEmpty()) {
        	 return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
	        			HttpStatus.BAD_REQUEST.value(), null, "please select a file!", request.getRequestURI()), HttpStatus.BAD_REQUEST);
        }

        try {
        	List<MultipartFile> files = Arrays.asList(uploadfile);
        
            for (MultipartFile file : files) {

                if (file.isEmpty()) {
                    continue; //next pls
                }
               if(file.getOriginalFilename().contains(" ")) {
            	   return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
   	        			HttpStatus.BAD_REQUEST.value(), null, "please, change file name, the name must not have empty spaces", request.getRequestURI()), HttpStatus.BAD_REQUEST);
               }
            	   
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER +currentUser.getUsername()+"/"+file.getOriginalFilename());
               Files.write(path,bytes);
            //saveUploadedFiles(Arrays.asList(uploadfile), currentUser);
             Material material = new Material();
       		
       		//set name for a material
       		material.setRealName(file.getOriginalFilename());
       		
       		//il file viene caricato nel mio pc, nel db viene inserito una stringa con il path del file 
       		//path del file
       		material.setServerFile(path.toString());
       		
       		materialRepository.save(material);
       		
       		//add a material created in a set of material in to argument
       		argument.get().setMaterialAdd(material);
       		argumentRepository.save(argument.get());
            } 
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
        		HttpStatus.OK.value(), null, "Material successfully", request.getRequestURI()), HttpStatus.OK);	

    }
   
   @Autowired
   private ServletContext servletContext;
   @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
   @GetMapping("/dowload1")
   public ResponseEntity<InputStreamResource> downloadFile(@RequestParam(value = "idMaterial") Long id) throws IOException {
   	
	   Optional<Material> material = materialRepository.findById(id);
	   
       MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, material.get().getRealName());

	   File file = new File(material.get().getServerFile());
	   
	   InputStreamResource resource = new InputStreamResource(new FileInputStream(file));       
       
       return ResponseEntity.ok()
               // Content-Disposition
               .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
               // Content-Type
               .contentType(mediaType)
               // Contet-Length
               .contentLength(file.length()) //
               
               .body(resource);
   }
    
    @DeleteMapping("/delete")
   // @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
    public ResponseEntity<ApiResponse> deleteFile(HttpServletRequest request,@CurrentUser UserPrincipal currentUser,@RequestParam(value = "idMaterial") Long idMaterial, @RequestParam(value = "idArgument") Long idArgument) {
    
    	Optional<UserProfessor> userProfessor = userProfessorRepository.findByUsername(currentUser.getUsername());
		 
    	Set<Argument> argumentUserProfessor = userProfessor.get().getArguments();
    
    	for(Argument a: argumentUserProfessor) {
    		if( a.getId() == idArgument) {
    			for(Material m : a.getMaterials()) {
    				
    				if(m.getId() == idMaterial) {
    				Path path = Paths.get(m.getServerFile());
    				try {
        				Files.delete(path);
    				}catch (IOException e) {
    					return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
    		        			HttpStatus.BAD_REQUEST.value(), null, "file not deleted", request.getRequestURI()), HttpStatus.BAD_REQUEST);    
    					}
    				a.setMaterialDel(m);
    				argumentRepository.save(a);
    				    				
    				}
    			}
    		}

    	}
		materialRepository.deleteById(idMaterial);

        return new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(), 
        		HttpStatus.OK.value(), null, "Material deleted", request.getRequestURI()), HttpStatus.OK);	
    }
    
	//role: admin, user
	//delete a one material
	@DeleteMapping("/deleteMaterialInArgument")
    @PreAuthorize("hasRole('ADMIN') or harRole('PROFESSOR')")
	public ResponseEntity<ApiResponse> deleteMaterialInArgument(@Valid @RequestBody MaterialRequest materialRequest,HttpServletRequest request) {
		

		Optional<Argument> argument = argumentRepository.findById(materialRequest.getIdArgument());
		//check if userStudent exist
		 if(!argument.isPresent()) {
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
			 if(!argument.isPresent()) {
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
				materialRepository.deleteById(m.getId());
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
