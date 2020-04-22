package it.cgm.planner.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cgm.planner.model.Argument;
import it.cgm.planner.model.Exam;


public interface ExamRepository extends JpaRepository<Exam, Long>{

	 List<Exam> findByargument(Optional<Argument> argument);

}
