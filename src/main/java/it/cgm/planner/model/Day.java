package it.cgm.planner.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "day")
public class Day {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
    @Column(length = 60)
    private DayName name;
	
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "day_hours",
    joinColumns = @JoinColumn(name = "day_id"),
    inverseJoinColumns = @JoinColumn(name = "hour_id"))
    private Set<Hour> hours = new HashSet<>();

    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DayName getName() {
		return name;
	}

	public void setName(DayName name) {
		this.name = name;
	}

	public Set<Hour> getHours() {
		return hours;
	}

	public void setHours(Set<Hour> hours) {
		this.hours = hours;
	}
    
	//method for add hours in a set 
	public void setHourAdd(Hour hour) {
		hours.add(hour);
		 }
	//method for add collection of hours in a set
	public void setHourAddCollection(Set<Hour> hour) {
		hours.addAll(hour);
	 }
	//method for delete hour in a set 
	public void sethoursDel(Hour hour) {
		hours.remove(hour);
	 }
	//method for delete all hours  in a set 
	public void setUsersDelCollection() {
		hours.removeAll(hours);
	 }
	//method for add List of hours in a set
	public void setHourAddCollection(List<Hour> hour) {
		hours.addAll(hour);
	 }
}
