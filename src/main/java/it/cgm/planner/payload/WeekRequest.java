package it.cgm.planner.payload;

import it.cgm.planner.model.DayName;
import it.cgm.planner.model.HourName;
import it.cgm.planner.model.WeekName;

//class for operation crud in week entity
public class WeekRequest {

	private Long idWeek;
	
	private WeekName name;
	
	private Long idGroup;

	private DayName dayName;
	
	private HourName hourName;
	
	private Long idArgument;
	 
	private Long idUserProfessor;
	 
	private Long idRoom;
	
	public WeekName getName() {
		return name;
	}

	public void setName(WeekName name) {
		this.name = name;
	}

	public Long getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(Long idGroup) {
		this.idGroup = idGroup;
	}

	public Long getIdArgument() {
		return idArgument;
	}

	public void setIdArgument(Long idArgument) {
		this.idArgument = idArgument;
	}

	public Long getIdUserProfessor() {
		return idUserProfessor;
	}

	public void setIdUserProfessor(Long idUserProfessor) {
		this.idUserProfessor = idUserProfessor;
	}

	public Long getIdRoom() {
		return idRoom;
	}

	public void setIdRoom(Long idRoom) {
		this.idRoom = idRoom;
	}

	public DayName getDayName() {
		return dayName;
	}

	public void setDayName(DayName dayName) {
		this.dayName = dayName;
	}

	public HourName getHourName() {
		return hourName;
	}

	public void setHourName(HourName hourName) {
		this.hourName = hourName;
	}

	public Long getIdWeek() {
		return idWeek;
	}

	public void setIdWeek(Long idWeek) {
		this.idWeek = idWeek;
	}
	
	
}
