package it.cgm.planner.payload;

import it.cgm.planner.model.HourName;

//class for operation crud in hour entity
public class HourRequest {
		
	    private Long idHour;
	    
		private Long idArgument;
		 
		private Long idUserProfessor;
		 
		private Long idRoom;

		private HourName name; 
		
		public Long getIdHour() {
			return idHour;
		}

		public void setIdHour(Long idHour) {
			this.idHour = idHour;
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

		public HourName getName() {
			return name;
		}

		public void setName(HourName name) {
			this.name = name;
		}
		
		
}
