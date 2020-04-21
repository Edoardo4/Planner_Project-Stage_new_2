package it.cgm.planner.payload;

public class GradeRequest {

	private Long idUserStudent;
	
	private Long idArgument;
	
	private int vote;

	private Long idGrade;
	
	public Long getIdUserStudent() {
		return idUserStudent;
	}

	public void setIdUserStudent(Long idUserStudent) {
		this.idUserStudent = idUserStudent;
	}

	public Long getIdArgument() {
		return idArgument;
	}

	public void setIdArgument(Long idArgument) {
		this.idArgument = idArgument;
	}

	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}

	public Long getIdGrade() {
		return idGrade;
	}

	public void setIdGrade(Long idGrade) {
		this.idGrade = idGrade;
	}

	
	
	
	
}
