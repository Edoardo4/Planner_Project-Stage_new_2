package it.cgm.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cgm.planner.model.UserProfessor;
import it.cgm.planner.model.UserStudent;

public interface UserProfessorRepository extends JpaRepository<UserProfessor, Long>{

	
}
