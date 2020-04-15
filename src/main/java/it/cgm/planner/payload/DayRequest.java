package it.cgm.planner.payload;

import java.util.List;

import it.cgm.planner.model.DayName;

//class for operation crud in day entity
public class DayRequest {

	private long idDay;
	
	private List<Long> idHours;

	private DayName name; 
	
	public long getIdDay() {
		return idDay;
	}

	public void setIdDay(long idDay) {
		this.idDay = idDay;
	}

	public List<Long> getIdHours() {
		return idHours;
	}

	public void setIdHours(List<Long> idHours) {
		this.idHours = idHours;
	}

	public DayName getName() {
		return name;
	}

	public void setName(DayName name) {
		this.name = name;
	}

	
	
	
}
