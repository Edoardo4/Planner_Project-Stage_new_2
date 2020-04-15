package it.cgm.planner.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    
}

