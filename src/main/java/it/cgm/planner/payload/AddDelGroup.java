package it.cgm.planner.payload;

import java.util.List;

//class to insert and delete user students in groups
public class AddDelGroup {

	private List<Long> idUser;
	
	private Long idGroup;
	
	public AddDelGroup() {
	}

	public AddDelGroup(List<Long> idUser, Long idGroup) {
		
		this.idUser = idUser;
		this.idGroup = idGroup;
	}

	public List<Long> getIdUser() {
		return idUser;
	}

	public void setIdUser(List<Long> idUser) {
		this.idUser = idUser;
	}

	public Long getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(Long idGroup) {
		this.idGroup = idGroup;
	}

	



	

	
	
}