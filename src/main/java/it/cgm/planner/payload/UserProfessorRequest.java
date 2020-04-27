package it.cgm.planner.payload;

public class UserProfessorRequest {

	private Long id;
    private String username;
   
    private Long idGroup;
    
	private Long idArgumentExam;
	public String name;
	public String content;
	private int vote;
	private Long idExam;
	
	private Long idArgument;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(Long idGroup) {
		this.idGroup = idGroup;
	}

	public Long getIdArgumentExam() {
		return idArgumentExam;
	}

	public void setIdArgumentExam(Long idArgumentExam) {
		this.idArgumentExam = idArgumentExam;
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

	public Long getIdArgument() {
		return idArgument;
	}

	public void setIdArgument(Long idArgument) {
		this.idArgument = idArgument;
	}
	
	

}
