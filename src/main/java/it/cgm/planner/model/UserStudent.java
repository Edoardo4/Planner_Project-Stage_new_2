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
@Table(name = "users_student", uniqueConstraints = {
		@UniqueConstraint(columnNames = {
				"username"
	        })
})
public class UserStudent  {

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
  	@JoinTable(name = "users_student_exam",
  	            joinColumns = @JoinColumn(name = "users_student_id"),
  	            inverseJoinColumns = @JoinColumn(name = "exam_id"))
  	 
      private Set<Exam> exams = new HashSet<>();
    
    @OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_student_grade",
	            joinColumns = @JoinColumn(name = "users_student_id"),
	            inverseJoinColumns = @JoinColumn(name = "grade_id"))
	 
    private Set<Grade> grades = new HashSet<>();
    
	public UserStudent() {
	}

	public UserStudent(@NotBlank @Size(max = 40) String name, @NotBlank @Size(max = 60) String lastname,
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

	public Set<Exam> getExams() {
		return exams;
	}

	public void setExams(Set<Exam> exams) {
		this.exams = exams;
	}

	public Set<Grade> getGrades() {
		return grades;
	}

	public void setGrades(Set<Grade> grades) {
		this.grades = grades;
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
		
	
	
	//method for add grade in a set 
	public void setGradesAdd(Grade grade) {
		grades.add(grade);
		 }
	//method for add collection of grade in a set
	public void setGradeAddCollection(Set<Grade> grade) {
		grades.addAll(grade);
	 }
	//method for delete grade in a set 
	public void setGradeDel(Grade grade) {
		grades.remove(grade);
	 }
	//method for delete all hours  in a set 
	public void setGradeDelCollection() {
		grades.removeAll(grades);
	 }
	//method for add List of grade in a set
	public void listGradeAddCollection(List<Grade> grade) {
		grades.addAll(grade);
	 }
}

