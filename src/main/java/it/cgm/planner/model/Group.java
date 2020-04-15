package it.cgm.planner.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "class", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "name"
        })
})

public class Group {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name")
	@NotNull @NotBlank
	@Size(max = 40)
	private String name;
	

	@Column(name = "is_valid", nullable = false, columnDefinition = "tinyint(1)")
	private boolean isValid = true;
	
	 @OneToMany(fetch = FetchType.EAGER)
	 @JoinTable(name = "class_users",
	            joinColumns = @JoinColumn(name = "class_id"),
	            inverseJoinColumns = @JoinColumn(name = "user_id"))
	 
	   private Set<UserStudent> users = new HashSet<>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public Set<UserStudent> getUsers() {
		return users;
	}

	public void setUsers1(Set<UserStudent> set) {
		this.users = set;
	}
	//method for add user in a set 
	public void setUsersAdd(UserStudent user) {
		     users.add(user);
		 }
	//method for add collection of user student in a set
	public void setUsersAddCollection(Set<UserStudent> users) {
	     users.addAll(users);
	 }
	//method for delete user in a set 
	public void setUsersDel(UserStudent user) {
		users.remove(user);
	 }
	//method for delete all user students in a set 
	public void setUsersDelCollection() {
		users.removeAll(users);
	 }
}
