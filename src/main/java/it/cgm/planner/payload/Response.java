package it.cgm.planner.payload;

import java.time.Instant;

import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;

@MappedSuperclass
public class Response<T> {
	
	@CreatedDate
	private Instant timestamp;
	private String status;
	private String error;
	private String message;
	private String path;
	private T data;
	
	
	public Instant getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
}
