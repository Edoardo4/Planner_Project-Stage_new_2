package it.cgm.planner.payload;

public class MaterialRequest {

	public Long idArgument;
	
	public String realName;
	
	public String serverFile;

	public Long idMaterial;
	
	public Long getIdArgument() {
		return idArgument;
	}

	public void setIdArgument(Long idArgument) {
		this.idArgument = idArgument;
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

	public Long getIdMaterial() {
		return idMaterial;
	}

	public void setIdMaterial(Long idMaterial) {
		this.idMaterial = idMaterial;
	}
	
	
}
