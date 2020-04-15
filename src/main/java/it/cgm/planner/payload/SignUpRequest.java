package it.cgm.planner.payload;

import javax.validation.constraints.*;

import it.cgm.planner.model.RoleName;

public class SignUpRequest {
    @NotBlank
    @Size(min = 4, max = 40)
    private String name;

    @NotBlank
    @Size(min = 1, max = 60)
    private String lastname;
    
    @NotBlank
    @Size(max = 100)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
    
    private RoleName roleName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public RoleName getRoleName() {
		return roleName;
	}

	public void setRoleName(RoleName roleName) {
		this.roleName = roleName;
	}
	
//viene creato automaticamente nel metodo signup
/*
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
    */
}
