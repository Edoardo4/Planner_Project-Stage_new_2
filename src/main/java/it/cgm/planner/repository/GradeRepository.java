package it.cgm.planner.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cgm.planner.model.Argument;
import it.cgm.planner.model.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long>{

	
	 List<Grade> findByargument(Optional<Argument> argument);
	 


}
