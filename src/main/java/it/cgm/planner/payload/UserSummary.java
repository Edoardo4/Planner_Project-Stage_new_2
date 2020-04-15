package it.cgm.planner.payload;

public class UserSummary {
    private Long id;
    private String lastname;
    private String name;
    private String username;

    public UserSummary(Long id, String lastname, String name, String username) {
        this.id = id;
        this.lastname = lastname;
        this.name = name;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setUsername(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getUsername() {
		return username;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
}
