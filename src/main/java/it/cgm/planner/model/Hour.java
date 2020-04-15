package it.cgm.planner.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "hours")
public class Hour {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
    @Column(length = 60)
    private HourName name;
	
	@ManyToOne(fetch = FetchType.EAGER)
	 private Argument argument;
	 
	 @ManyToOne(fetch = FetchType.EAGER)
	 private UserProfessor userProfessor;
	 
	 @ManyToOne(fetch = FetchType.EAGER)
	 private Room room;
	 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HourName getName() {
		return name;
	}

	public void setName(HourName name) {
		this.name = name;
	}

	public Argument getArgument() {
		return argument;
	}

	public void setArgument(Argument argument) {
		this.argument = argument;
	}

	public UserProfessor getUserProfessor() {
		return userProfessor;
	}

	public void setUserProfessor(UserProfessor userProfessor) {
		this.userProfessor = userProfessor;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	
	 
	 
}
