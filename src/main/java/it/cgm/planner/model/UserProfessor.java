package it.cgm.planner.model;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users_professor", uniqueConstraints = {
		@UniqueConstraint(columnNames = {
				"username"
	        })
})
public class UserProfessor  {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 60)
    private String lastname;
    
    //@NotBlank
    @Size(max = 65)
    private String username;

    @OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_professor_group",
	            joinColumns = @JoinColumn(name = "users_professor_id"),
	            inverseJoinColumns = @JoinColumn(name = "class_id"))
	 
    private Set<Group> groups = new HashSet<>();
    
    
    @OneToMany(fetch = FetchType.EAGER)
  	@JoinTable(name = "users_professor_argument",
  	            joinColumns = @JoinColumn(name = "users_professor_id"),
  	            inverseJoinColumns = @JoinColumn(name = "argument_id"))
  	 
      private Set<Argument> arguments = new HashSet<>();
    
    
    @OneToMany(fetch = FetchType.EAGER)
  	@JoinTable(name = "users_professor_exam",
  	            joinColumns = @JoinColumn(name = "users_professor_id"),
  	            inverseJoinColumns = @JoinColumn(name = "exam_id"))
  	 
      private Set<Exam> exams = new HashSet<>();
    
	public UserProfessor() {
	}

	public UserProfessor(@NotBlank @Size(max = 40) String name, @NotBlank @Size(max = 60) String lastname,
			@Size(max = 65) String username) {
		super();
		this.name = name;
		this.lastname = lastname;
		this.username = username;
	}

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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	//method for add group in a set 
	public void setGroupAdd(Group group) {
		groups.add(group);
		 }
	//method for add collection of group in a set
	public void setGroupAddCollection(Set<Group> group) {
		groups.addAll(group);
	 }
	//method for delete group in a set 
	public void setGroupDel(Group group) {
		groups.remove(group);
	 }
	//method for delete all group  in a set 
	public void setGroupDelCollection() {
		groups.removeAll(groups);
	 }
	//method for add List of group in a set
	public void ListGroupAddCollection(List<Group> group) {
		groups.addAll(group);
	 }
	
	
	public Set<Argument> getArguments() {
		return arguments;
	}
	
	public void setArguments(Set<Argument> arguments) {
		this.arguments = arguments;
	}
	//method for add argument in a set 
	public void setArgumentAdd(Argument argument) {
		arguments.add(argument);
		 }
	//method for add collection of argument in a set
	public void setArgumentAddCollection(Set<Argument> argument) {
		arguments.addAll(argument);
	 }
	//method for delete argument in a set 
	public void setArgumentDel(Argument argument) {
		arguments.remove(argument);
	 }
	//method for delete all argument  in a set 
	public void setArgumentDelCollection() {
		arguments.removeAll(arguments);
	 }
	//method for add List of argument in a set
	public void ListArgumentAddCollection(List<Argument> argument) {
		arguments.addAll(argument);
	 }
	
	
	public Set<Exam> getExams() {
		return exams;
	}

	public void setExams(Set<Exam> exams) {
		this.exams = exams;
	}

	//method for add exam in a set 
	public void setExamAdd(Exam exam) {
		exams.add(exam);
		 }
	//method for add collection of exam in a set
	public void setExamAddCollection(Set<Exam> exam) {
		exams.addAll(exam);
	 }
	//method for delete exam in a set 
	public void setExamDel(Exam exam) {
		exams.remove(exam);
	 }
	//method for delete all exam  in a set 
	public void setExamDelCollection() {
		exams.removeAll(exams);
	 }
	//method for add List of exam in a set
	public void ListExamAddCollection(List<Exam> exam) {
		exams.addAll(exam);
	 }
}

