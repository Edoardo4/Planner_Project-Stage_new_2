package it.cgm.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cgm.planner.model.Day;
import it.cgm.planner.model.DayName;

public interface DayRepository extends JpaRepository<Day, Long>{

	boolean existsByname(DayName name);

}
