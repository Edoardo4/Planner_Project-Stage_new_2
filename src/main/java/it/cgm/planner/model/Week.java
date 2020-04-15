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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "weeks")
public class Week {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="class_name")
	private String group;
	
	@Enumerated(EnumType.STRING)
    @Column(length = 60)
    private WeekName name;
	
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "week_day",
    joinColumns = @JoinColumn(name = "weeks_id"),
    inverseJoinColumns = @JoinColumn(name = "day_id"))
    private Set<Day> days = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public WeekName getName() {
		return name;
	}

	public void setName(WeekName name) {
		this.name = name;
	}


	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Set<Day> getDays() {
		return days;
	}

	public void setDays(Set<Day> days) {
		this.days = days;
	}
	
	//Method for add hours in a set 
	public void setDayAdd(Day day) {
		days.add(day);
		 }
	//method for add collection of hours student in a set
	public void setDayAddCollection(List<Day> day) {
		days.addAll(day);
	 }
	//method for delete hour in a set 
	public void setDayDel(Day day) {
		days.remove(day);
	 }
	//method for delete all hour students in a set 
	public void setDayDelCollection() {
		days.removeAll(days);
	 }
    
}
