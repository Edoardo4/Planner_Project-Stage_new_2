package it.cgm.planner.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import it.cgm.planner.model.Week;
import it.cgm.planner.model.WeekName;

public interface WeekRepository extends JpaRepository<Week, Long> {


	Week findByname(WeekName weekName);

}
