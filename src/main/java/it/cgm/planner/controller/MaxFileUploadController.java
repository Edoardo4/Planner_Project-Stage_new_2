package it.cgm.planner.controller;

import java.time.Instant;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import it.cgm.planner.payload.ApiResponse;


@RestController
@ControllerAdvice
public class MaxFileUploadController {
	    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAllExceptions(Exception ex, HttpServletResponse response) throws Exception {
	    if( ex instanceof MaxUploadSizeExceededException == false) {
	    	throw ex;
	    }else {
	    	long MAX_FILE_SIZE = 15728640; //15Mb
	    	long MEGABYTE = 1024L * 1024L;
	    	ResponseEntity<ApiResponse> responsePayload =
	    			new ResponseEntity<ApiResponse>(new ApiResponse(Instant.now(),
    					 HttpStatus.INTERNAL_SERVER_ERROR.value(), null,
    					 "The selected file is greater than "+MAX_FILE_SIZE / MEGABYTE+" MB", null),
    					 HttpStatus.INTERNAL_SERVER_ERROR);
	      return responsePayload;
	    }
    }
    
}
