package it.cgm.planner.payload;

//import java.time.Instant;

public class UserProfile {
    private Long id;
    private String username;
    private String name;
    private String lastname;
    //private Instant joinedAt;
    
    public UserProfile(Long id, String username, String name, String lastname) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.lastname = lastname;
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

}
