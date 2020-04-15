package it.cgm.planner.payload;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

//class for operation crud in group entity
public class GroupRequest {

	@NotBlank
    @Size(min = 4, max = 40)
    private String name;

	@Column(name = "is_valid", nullable = false, columnDefinition = "tinyint(1)")
	private boolean isValid = true;
	
    private Long idUser;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}


	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}


    
}
