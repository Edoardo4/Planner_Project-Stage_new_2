package it.cgm.planner.payload;

public class MaterialRequest {

	public Long idArgument;
	
	public String nameMaterial;
	
	public String content;

	public Long idMaterial;
	
	public Long getIdArgument() {
		return idArgument;
	}

	public void setIdArgument(Long idArgument) {
		this.idArgument = idArgument;
	}

	public String getNameMaterial() {
		return nameMaterial;
	}

	public void setNameMaterial(String nameMaterial) {
		this.nameMaterial = nameMaterial;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getIdMaterial() {
		return idMaterial;
	}

	public void setIdMaterial(Long idMaterial) {
		this.idMaterial = idMaterial;
	}
	
	
}
