package it.cgm.planner.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//ogni material andrà in una folder, ogni professore avrà la sua
@Entity
@Table(name = "material")
public class Material {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	//name that the professor assigns
	@Column(name="realName")
	@NotNull @NotBlank
	@Size(max = 40)
	private String realName;
	
	//name professor/date.extenction
	@Column(name="serverFile")
	@NotNull @NotBlank
	@Size(max = 100)
	private String serverFile;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getServerFile() {
		return serverFile;
	}

	public void setServerFile(String serverFile) {
		this.serverFile = serverFile;
	}
	
}
