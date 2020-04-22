package it.cgm.planner.payload;

public class ExamRequest {

	private Long idUserStudent;
	
	private Long idArgument;
	
	public String name;
	
	public String content;
	
	private int vote;

	private Long idExam;

	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}

	public Long getIdExam() {
		return idExam;
	}

	public void setIdExam(Long idExam) {
		this.idExam = idExam;
	}

	
}
